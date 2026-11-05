package iterator;

import java.util.List;

import domain.Sale;

public class SalesIterator implements ExtendedIterator<Sale> {
	private final List<Sale> sales;
    private int position = 0; 

    public SalesIterator(List<Sale> sales) {
        this.sales = sales;
    }

    @Override
    public boolean hasNext() {
        return position < sales.size();
    }

    @Override
    public Sale next() {
        return sales.get(position++);
    }

    @Override
    public boolean hasPrevious() {
        return position > 0;
    }

    @Override
    public Sale previous() {
        return sales.get(--position);
    }

    @Override
    public void goFirst() {
        position = 0;
    }

    @Override
    public void goLast() {
        position = sales.size();
    }

}
