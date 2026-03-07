package ru.mephi.lab3.task4;

public interface IntSequence  {
    boolean hasNext();
    int next();

    static IntSequence of(int... values){
        return new IntSequence (){
            private int i = 0;

            public boolean hasNext() {
                    return i < values.length;
            }

            public int next() {
                    return values[i++];
            }
        };
    }
}
