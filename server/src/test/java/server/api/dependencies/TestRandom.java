package server.api.dependencies;

import lombok.Getter;
import lombok.Setter;

import java.util.Random;


/**
 * Implementation of Random for testing
 * Is not random, counts up from 0
 */
public class TestRandom extends Random {

    @Setter
    @Getter
    long count = 0;

    @Override
    public long nextLong(){
        return count++;
    }

    @Override
    public int nextInt() { return (int) count++; }
}
