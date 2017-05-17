package cml.language.foundation;

import java.util.Objects;

import static java.util.Objects.hash;

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

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?> pair = (Pair<?>) o;
        return (Objects.equals(left, pair.left) &&
                Objects.equals(right, pair.right)) ||
               (Objects.equals(left, pair.right) &&
                Objects.equals(right, pair.left));
    }

    @Override
    public int hashCode()
    {
        final int h1 = left == null ? 0 : left.hashCode();
        final int h2 = right == null ? 0 : right.hashCode();

        return h1 > h2 ? hash(h2, h1) : hash(h1, h2);
    }
}
