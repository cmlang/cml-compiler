package functions.poj;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;
import org.jooq.lambda.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static org.jooq.lambda.Seq.*;

public class Functions
{
    private final Item requiredItem;
    private final @Nullable Item singleItem;
    private final List<Item> items;

    public Functions(Item requiredItem, @Nullable Item singleItem, List<Item> items)
    {
        this.requiredItem = requiredItem;
        this.singleItem = singleItem;
        this.items = items;
    }

    public Item getRequiredItem()
    {
        return this.requiredItem;
    }

    public Optional<Item> getSingleItem()
    {
        return Optional.ofNullable(this.singleItem);
    }

    public List<Item> getItems()
    {
        return Collections.unmodifiableList(this.items);
    }

    public boolean isEmptyItems()
    {
        return seq(getItems()).isEmpty();
    }

    public boolean isPresentItems()
    {
        return !seq(getItems()).isEmpty();
    }

    public boolean isEmptySingleItem()
    {
        return !getSingleItem().isPresent();
    }

    public boolean isPresentSingleItem()
    {
        return getSingleItem().isPresent();
    }

    public boolean isRequiredEmptySingleItem()
    {
        return getRequiredItem() == null;
    }

    public boolean isRequiredPresentSingleItem()
    {
        return getRequiredItem() != null;
    }

    public Optional<Item> getItemsFirst()
    {
        return seq(getItems()).findFirst();
    }

    public Optional<Item> getItemsLast()
    {
        return seq(getItems()).findLast();
    }

    public Optional<Item> getSingleItemFirst()
    {
        return seq(getSingleItem()).findFirst();
    }

    public Optional<Item> getSingleItemLast()
    {
        return seq(getSingleItem()).findLast();
    }

    public Optional<Item> getRequiredItemFirst()
    {
        return seq(asList(getRequiredItem())).findFirst();
    }

    public Optional<Item> getRequiredItemLast()
    {
        return seq(asList(getRequiredItem())).findLast();
    }

    public boolean isAtLeastOneLargeItem()
    {
        return seq(getItems()).anyMatch(item -> (item.getSize() > 100));
    }

    public boolean isAllLargeItems()
    {
        return seq(getItems()).allMatch(item -> (item.getSize() > 100));
    }

    public boolean isLargeItemExists()
    {
        return seq(getSingleItem()).anyMatch(item -> (item.getSize() > 100));
    }

    public boolean isLargeItemAll()
    {
        return seq(getSingleItem()).allMatch(item -> (item.getSize() > 100));
    }

    public boolean isRequiredItemExists()
    {
        return seq(asList(getRequiredItem())).anyMatch(item -> (item.getSize() > 100));
    }

    public boolean isRequiredItemAll()
    {
        return seq(asList(getRequiredItem())).allMatch(item -> (item.getSize() > 100));
    }

    public List<Item> getItemsSelect()
    {
        return seq(getItems()).filter(item -> (item.getSize() > 100)).toList();
    }

    public List<Item> getItemsReject()
    {
        return seq(getItems()).removeAll(seq(getItems()).filter(item -> (item.getSize() > 100))).toList();
    }

    public List<Item> getSingleItemSelect()
    {
        return seq(getSingleItem()).filter(item -> (item.getSize() > 100)).toList();
    }

    public List<Item> getSingleItemReject()
    {
        return seq(getSingleItem()).removeAll(seq(getSingleItem()).filter(item -> (item.getSize() > 100))).toList();
    }

    public List<Item> getRequiredItemSelect()
    {
        return seq(asList(getRequiredItem())).filter(item -> (item.getSize() > 100)).toList();
    }

    public List<Item> getRequiredItemReject()
    {
        return seq(asList(getRequiredItem())).removeAll(seq(asList(getRequiredItem())).filter(item -> (item.getSize() > 100))).toList();
    }

    public String toString()
    {
        return new StringBuilder(Functions.class.getSimpleName())
                   .append('(')
                   .append("requiredItem=").append(String.format("\"%s\"", getRequiredItem())).append(", ")
                   .append("singleItem=").append(getSingleItem().isPresent() ? String.format("\"%s\"", getSingleItem()) : "not present").append(", ")
                   .append("emptyItems=").append(String.format("\"%s\"", isEmptyItems())).append(", ")
                   .append("presentItems=").append(String.format("\"%s\"", isPresentItems())).append(", ")
                   .append("emptySingleItem=").append(String.format("\"%s\"", isEmptySingleItem())).append(", ")
                   .append("presentSingleItem=").append(String.format("\"%s\"", isPresentSingleItem())).append(", ")
                   .append("requiredEmptySingleItem=").append(String.format("\"%s\"", isRequiredEmptySingleItem())).append(", ")
                   .append("requiredPresentSingleItem=").append(String.format("\"%s\"", isRequiredPresentSingleItem())).append(", ")
                   .append("atLeastOneLargeItem=").append(String.format("\"%s\"", isAtLeastOneLargeItem())).append(", ")
                   .append("allLargeItems=").append(String.format("\"%s\"", isAllLargeItems())).append(", ")
                   .append("largeItemExists=").append(String.format("\"%s\"", isLargeItemExists())).append(", ")
                   .append("largeItemAll=").append(String.format("\"%s\"", isLargeItemAll())).append(", ")
                   .append("requiredItemExists=").append(String.format("\"%s\"", isRequiredItemExists())).append(", ")
                   .append("requiredItemAll=").append(String.format("\"%s\"", isRequiredItemAll()))
                   .append(')')
                   .toString();
    }
}