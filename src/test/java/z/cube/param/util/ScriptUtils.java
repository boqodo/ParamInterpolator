/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package z.cube.param.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;


/**
 * Generic utility methods for working with SQL scripts. Mainly for internal use
 * within the framework.
 *
 * @author Thomas Risberg
 * @author Sam Brannen
 * @author Juergen Hoeller
 * @author Keith Donald
 * @author Dave Syer
 * @author Chris Beams
 * @author Oliver Gierke
 * @author Chris Baldwin
 * @since 4.0.3
 */
public abstract class ScriptUtils {

    private static final Logger logger = LoggerFactory.getLogger(ScriptUtils.class);

    /**
     * Default statement separator within SQL scripts: {@code ";"}.
     */
    public static final String DEFAULT_STATEMENT_SEPARATOR = ";";

    /**
     * Fallback statement separator within SQL scripts: {@code "\n"}.
     * <p>Used if neither a custom separator nor the
     * {@link #DEFAULT_STATEMENT_SEPARATOR} is present in a given script.
     */
    public static final String FALLBACK_STATEMENT_SEPARATOR = "\n";

    public static final String EOF_STATEMENT_SEPARATOR = "^^^ END OF SCRIPT ^^^";

    /**
     * Default prefix for single-line comments within SQL scripts: {@code "--"}.
     */
    public static final String DEFAULT_COMMENT_PREFIX = "--";

    /**
     * Default start delimiter for block comments within SQL scripts: {@code "/*"}.
     */
    public static final String DEFAULT_BLOCK_COMMENT_START_DELIMITER = "/*";

    /**
     * Default end delimiter for block comments within SQL scripts: <code>"*&#47;"</code>.
     */
    public static final String DEFAULT_BLOCK_COMMENT_END_DELIMITER = "*/";


    /**
     * Prevent instantiation of this utility class.
     */
    private ScriptUtils() {
		/* no-op */
    }

	public static void splitSqlScript(String script, char separator, List<String> statements) {
		splitSqlScript(script, String.valueOf(separator), statements);
	}


	public static void splitSqlScript(String script, String separator, List<String> statements) {
		splitSqlScript(null, script, separator, DEFAULT_COMMENT_PREFIX, DEFAULT_BLOCK_COMMENT_START_DELIMITER,
			DEFAULT_BLOCK_COMMENT_END_DELIMITER, statements);
	}


	public static void splitSqlScript(Reader reader, String script, String separator, String commentPrefix,
			String blockCommentStartDelimiter, String blockCommentEndDelimiter, List<String> statements) {

		Validate.notBlank(script, "script must not be null or empty");
        Validate.notNull(separator, "separator must not be null");
        Validate.notBlank(commentPrefix, "commentPrefix must not be null or empty");
        Validate.notBlank(blockCommentStartDelimiter, "blockCommentStartDelimiter must not be null or empty");
        Validate.notBlank(blockCommentEndDelimiter, "blockCommentEndDelimiter must not be null or empty");

		StringBuilder sb = new StringBuilder();
		boolean inLiteral = false;
		boolean inEscape = false;
		char[] content = script.toCharArray();
		for (int i = 0; i < script.length(); i++) {
			char c = content[i];
			if (inEscape) {
				inEscape = false;
				sb.append(c);
				continue;
			}
			// MySQL style escapes
			if (c == '\\') {
				inEscape = true;
				sb.append(c);
				continue;
			}
			if (c == '\'') {
				inLiteral = !inLiteral;
			}
			if (!inLiteral) {
				if (script.startsWith(separator, i)) {
					// we've reached the end of the current statement
					if (sb.length() > 0) {
						statements.add(sb.toString());
						sb = new StringBuilder();
					}
					i += separator.length() - 1;
					continue;
				}
				else if (script.startsWith(commentPrefix, i)) {
					// skip over any content from the start of the comment to the EOL
					int indexOfNextNewline = script.indexOf("\n", i);
					if (indexOfNextNewline > i) {
						i = indexOfNextNewline;
						continue;
					}
					else {
						// if there's no EOL, we must be at the end
						// of the script, so stop here.
						break;
					}
				}
				else if (script.startsWith(blockCommentStartDelimiter, i)) {
					// skip over any block comments
					int indexOfCommentEnd = script.indexOf(blockCommentEndDelimiter, i);
					if (indexOfCommentEnd > i) {
						i = indexOfCommentEnd + blockCommentEndDelimiter.length() - 1;
						continue;
					}
					else {
                        String subMessage=String.format("Missing block comment end delimiter [%s].",
                                blockCommentEndDelimiter);
                        String message=String.format("Failed to parse SQL script from reader [%s]: %s", (reader == null ? "<unknown>"
                                : subMessage));
						throw new RuntimeException(message);
					}
				}
				else if (c == ' ' || c == '\n' || c == '\t') {
					// avoid multiple adjacent whitespace characters
					if (sb.length() > 0 && sb.charAt(sb.length() - 1) != ' ') {
						c = ' ';
					}
					else {
						continue;
					}
				}
			}
			sb.append(c);
		}
		if (StringUtils.isNotBlank(sb)) {
			statements.add(sb.toString());
		}
	}

	static String readScript(Reader reader) throws IOException {
		return readScript(reader, DEFAULT_COMMENT_PREFIX, DEFAULT_STATEMENT_SEPARATOR);
	}


	private static String readScript(Reader reader, String commentPrefix, String separator)
			throws IOException {
		LineNumberReader lnr = new LineNumberReader(reader);
		try {
			return readScript(lnr, commentPrefix, separator);
		}
		finally {
			lnr.close();
		}
	}

	/**
	 * Read a script from the provided {@code LineNumberReader}, using the supplied
	 * comment prefix and statement separator, and build a {@code String} containing
	 * the lines.
	 * <p>Lines <em>beginning</em> with the comment prefix are excluded from the
	 * results; however, line comments anywhere else &mdash; for example, within
	 * a statement &mdash; will be included in the results.
	 * @param lineNumberReader the {@code LineNumberReader} containing the script
	 * to be processed
	 * @param commentPrefix the prefix that identifies comments in the SQL script &mdash;
	 * typically "--"
	 * @param separator the statement separator in the SQL script &mdash; typically ";"
	 * @return a {@code String} containing the script lines
	 * @throws IOException in case of I/O errors
	 */
	public static String readScript(LineNumberReader lineNumberReader, String commentPrefix, String separator)
			throws IOException {
		String currentStatement = lineNumberReader.readLine();
		StringBuilder scriptBuilder = new StringBuilder();
		while (currentStatement != null) {
			if (commentPrefix != null && !currentStatement.startsWith(commentPrefix)) {
				if (scriptBuilder.length() > 0) {
					scriptBuilder.append('\n');
				}
				scriptBuilder.append(currentStatement);
			}
			currentStatement = lineNumberReader.readLine();
		}
		appendSeparatorToScriptIfNecessary(scriptBuilder, separator);
		return scriptBuilder.toString();
	}

	private static void appendSeparatorToScriptIfNecessary(StringBuilder scriptBuilder, String separator) {
		if (separator == null) {
			return;
		}
		String trimmed = separator.trim();
		if (trimmed.length() == separator.length()) {
			return;
		}
		// separator ends in whitespace, so we might want to see if the script is trying
		// to end the same way
		if (scriptBuilder.lastIndexOf(trimmed) == scriptBuilder.length() - trimmed.length()) {
			scriptBuilder.append(separator.substring(trimmed.length()));
		}
	}

	/**
	 * Does the provided SQL script contain the specified delimiter?
	 * @param script the SQL script
	 * @param delim String delimiting each statement - typically a ';' character
	 */
	public static boolean containsSqlScriptDelimiters(String script, String delim) {
		boolean inLiteral = false;
		char[] content = script.toCharArray();
		for (int i = 0; i < script.length(); i++) {
			if (content[i] == '\'') {
				inLiteral = !inLiteral;
			}
			if (!inLiteral && script.startsWith(delim, i)) {
				return true;
			}
		}
		return false;
	}

	public static void executeSqlScript(Connection connection, Reader reader){
		executeSqlScript(connection, reader, false, false, DEFAULT_COMMENT_PREFIX, DEFAULT_STATEMENT_SEPARATOR,
			DEFAULT_BLOCK_COMMENT_START_DELIMITER, DEFAULT_BLOCK_COMMENT_END_DELIMITER);
	}


	public static void executeSqlScript(Connection connection, Reader reader, boolean continueOnError,
			boolean ignoreFailedDrops, String commentPrefix, String separator, String blockCommentStartDelimiter,
			String blockCommentEndDelimiter)  {

		try {
			if (logger.isInfoEnabled()) {
				logger.info("Executing SQL script from " + reader);
			}

			long startTime = System.currentTimeMillis();
			List<String> statements = new LinkedList<String>();
			String script;
			try {
				script = readScript(reader, commentPrefix, separator);
			}
			catch (IOException ex) {
				throw new RuntimeException("Cannot read SQL script from " + reader);
			}

			if (separator == null) {
				separator = DEFAULT_STATEMENT_SEPARATOR;
			}
			if (!EOF_STATEMENT_SEPARATOR.equals(separator) && !containsSqlScriptDelimiters(script, separator)) {
				separator = FALLBACK_STATEMENT_SEPARATOR;
			}

			splitSqlScript(reader, script, separator, commentPrefix, blockCommentStartDelimiter,
				blockCommentEndDelimiter, statements);
			int lineNumber = 0;
			Statement stmt = connection.createStatement();
			try {
				for (String statement : statements) {
					lineNumber++;
					try {
						stmt.execute(statement);
						int rowsAffected = stmt.getUpdateCount();
						if (logger.isDebugEnabled()) {
							logger.debug(rowsAffected + " returned as updateCount for SQL: " + statement);
						}
					}
					catch (SQLException ex) {
						boolean dropStatement = StringUtils.startsWithIgnoreCase(statement.trim(), "drop");
						if (continueOnError || (dropStatement && ignoreFailedDrops)) {
							if (logger.isDebugEnabled()) {
								logger.debug("Failed to execute SQL script statement at line " + lineNumber
										+ " of reader " + reader + ": " + statement, ex);
							}
						}
						else {
                            String message="Failed to execute SQL script statement at line " + lineNumber + " of Reader " + reader + ": "
                                    + statement;
							throw new RuntimeException(message, ex);
						}
					}
				}
			}
			finally {
				try {
					stmt.close();
				}
				catch (Throwable ex) {
					logger.debug("Could not close JDBC Statement", ex);
				}
			}

			long elapsedTime = System.currentTimeMillis() - startTime;
			if (logger.isInfoEnabled()) {
				logger.info("Executed SQL script from " + reader + " in " + elapsedTime + " ms.");
			}
		}
		catch (Exception ex) {
			throw new RuntimeException(
				"Failed to execute database script from reader [" + reader + "]", ex);
		}
	}

}
