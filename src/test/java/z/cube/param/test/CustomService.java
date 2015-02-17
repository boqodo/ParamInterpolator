package z.cube.param.test;

import org.springframework.stereotype.Service;

@Service
public class CustomService {

    public Integer getDoubInt(@Doub Integer i) {
        return i;
    }

    public Integer getTrebleInt(@Treble Integer i) {
        return i;
    }

    public Integer getDoubAndTrebleInt(@Doub @Treble Integer i) {
        return i;
    }
}
