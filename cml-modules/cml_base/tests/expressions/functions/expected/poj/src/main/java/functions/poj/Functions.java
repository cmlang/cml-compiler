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
    private final List<Item> items2;

    public Functions(Item requiredItem, @Nullable Item singleItem, List<Item> items, List<Item> items2)
    {
        this.requiredItem = requiredItem;
        this.singleItem = singleItem;
        this.items = items;
        this.items2 = items2;
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

    public List<Item> getItems2()
    {
        return Collections.unmodifiableList(this.items2);
    }

    public boolean isEmptyItems()
    {
        return seq(this.getItems()).isEmpty();
    }

    public boolean isPresentItems()
    {
        return !seq(this.getItems()).isEmpty();
    }

    public boolean isEmptySingleItem()
    {
        return !this.getSingleItem().isPresent();
    }

    public boolean isPresentSingleItem()
    {
        return this.getSingleItem().isPresent();
    }

    public boolean isRequiredEmptySingleItem()
    {
        return this.getRequiredItem() == null;
    }

    public boolean isRequiredPresentSingleItem()
    {
        return this.getRequiredItem() != null;
    }

    public Optional<Item> getItemsFirst()
    {
        return seq(this.getItems()).findFirst();
    }

    public Optional<Item> getItemsLast()
    {
        return seq(this.getItems()).findLast();
    }

    public Optional<Item> getSingleItemFirst()
    {
        return seq(this.getSingleItem()).findFirst();
    }

    public Optional<Item> getSingleItemLast()
    {
        return seq(this.getSingleItem()).findLast();
    }

    public Optional<Item> getRequiredItemFirst()
    {
        return seq(asList(this.getRequiredItem())).findFirst();
    }

    public Optional<Item> getRequiredItemLast()
    {
        return seq(asList(this.getRequiredItem())).findLast();
    }

    public boolean isAtLeastOneLargeItem()
    {
        return seq(this.getItems()).anyMatch(item1 -> (item1.getSize() > 100));
    }

    public boolean isAllLargeItems()
    {
        return seq(this.getItems()).allMatch(item2 -> (item2.getSize() > 100));
    }

    public boolean isLargeItemExists()
    {
        return seq(this.getSingleItem()).anyMatch(item3 -> (item3.getSize() > 100));
    }

    public boolean isLargeItemAll()
    {
        return seq(this.getSingleItem()).allMatch(item4 -> (item4.getSize() > 100));
    }

    public boolean isRequiredItemExists()
    {
        return seq(asList(this.getRequiredItem())).anyMatch(item5 -> (item5.getSize() > 100));
    }

    public boolean isRequiredItemAll()
    {
        return seq(asList(this.getRequiredItem())).allMatch(item6 -> (item6.getSize() > 100));
    }

    public List<Item> getItemsSelect()
    {
        return seq(this.getItems()).filter(item7 -> (item7.getSize() > 100)).toList();
    }

    public List<Item> getItemsReject()
    {
        return seq(this.getItems()).removeAll(seq(this.getItems()).filter(item8 -> (item8.getSize() > 100))).toList();
    }

    public List<Item> getSingleItemSelect()
    {
        return seq(this.getSingleItem()).filter(item9 -> (item9.getSize() > 100)).toList();
    }

    public List<Item> getSingleItemReject()
    {
        return seq(this.getSingleItem()).removeAll(seq(this.getSingleItem()).filter(item10 -> (item10.getSize() > 100))).toList();
    }

    public List<Item> getRequiredItemSelect()
    {
        return seq(asList(this.getRequiredItem())).filter(item11 -> (item11.getSize() > 100)).toList();
    }

    public List<Item> getRequiredItemReject()
    {
        return seq(asList(this.getRequiredItem())).removeAll(seq(asList(this.getRequiredItem())).filter(item12 -> (item12.getSize() > 100))).toList();
    }

    public List<Integer> getItemsCollect()
    {
        return seq(this.getItems()).map(item13 -> item13.getSize()).toList();
    }

    public List<Integer> getSingleItemCollect()
    {
        return seq(this.getSingleItem()).map(item14 -> item14.getSize()).toList();
    }

    public List<Integer> getRequiredItemCollect()
    {
        return seq(asList(this.getRequiredItem())).map(item15 -> item15.getSize()).toList();
    }

    public List<Item> getSortedItems()
    {
        return seq(this.getItems()).sorted((i1, i2) -> (i1.getSize() < i2.getSize()) ? -1 : ((i2.getSize() < i1.getSize()) ? +1 : 0)).toList();
    }

    public List<Item> getReversedItems()
    {
        return seq(this.getItems()).reverse().toList();
    }

    public Item getNewItem()
    {
        return new Item(12);
    }

    public List<Item> getConcatItems()
    {
        return concat(seq(this.getItems()), seq(this.getItems2())).toList();
    }

    public long getCountItems()
    {
        return seq(this.getItems()).count();
    }

    public List<Item> getDistinctItems()
    {
        return seq(this.getItems()).distinct().toList();
    }

    public String toString()
    {
        return new StringBuilder(Functions.class.getSimpleName())
                   .append('(')
                   .append("requiredItem=").append(String.format("\"%s\"", this.getRequiredItem())).append(", ")
                   .append("singleItem=").append(this.getSingleItem().isPresent() ? String.format("\"%s\"", this.getSingleItem()) : "not present").append(", ")
                   .append("emptyItems=").append(String.format("\"%s\"", this.isEmptyItems())).append(", ")
                   .append("presentItems=").append(String.format("\"%s\"", this.isPresentItems())).append(", ")
                   .append("emptySingleItem=").append(String.format("\"%s\"", this.isEmptySingleItem())).append(", ")
                   .append("presentSingleItem=").append(String.format("\"%s\"", this.isPresentSingleItem())).append(", ")
                   .append("requiredEmptySingleItem=").append(String.format("\"%s\"", this.isRequiredEmptySingleItem())).append(", ")
                   .append("requiredPresentSingleItem=").append(String.format("\"%s\"", this.isRequiredPresentSingleItem())).append(", ")
                   .append("atLeastOneLargeItem=").append(String.format("\"%s\"", this.isAtLeastOneLargeItem())).append(", ")
                   .append("allLargeItems=").append(String.format("\"%s\"", this.isAllLargeItems())).append(", ")
                   .append("largeItemExists=").append(String.format("\"%s\"", this.isLargeItemExists())).append(", ")
                   .append("largeItemAll=").append(String.format("\"%s\"", this.isLargeItemAll())).append(", ")
                   .append("requiredItemExists=").append(String.format("\"%s\"", this.isRequiredItemExists())).append(", ")
                   .append("requiredItemAll=").append(String.format("\"%s\"", this.isRequiredItemAll())).append(", ")
                   .append("itemsCollect=").append(this.getItemsCollect()).append(", ")
                   .append("singleItemCollect=").append(this.getSingleItemCollect()).append(", ")
                   .append("requiredItemCollect=").append(this.getRequiredItemCollect()).append(", ")
                   .append("countItems=").append(String.format("\"%s\"", this.getCountItems()))
                   .append(')')
                   .toString();
    }
}