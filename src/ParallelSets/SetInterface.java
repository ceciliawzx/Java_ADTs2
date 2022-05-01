package ParallelSets;

public interface SetInterface<E> {

    boolean add(E item);

    boolean remove(E item);

    boolean contains(E item);

    int size();

}
