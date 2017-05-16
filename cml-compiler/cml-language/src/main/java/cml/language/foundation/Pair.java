package cml.language.foundation;

public class Pair<T extends ModelElement>
{
    private final T left;
    private final T right;

    public Pair(T left, T right)
    {
        this.left = left;
        this.right = right;
    }

    public T getLeft()
    {
        return left;
    }

    public T getRight()
    {
        return right;
    }
}
