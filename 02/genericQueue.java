public class genericQueue <ItemType>
{

    ItemType[] data;
    int totalNodes, size, front, rear;

    public genericQueue()
    {
        size = 1000;
        front = 0;
        rear = 0;
        totalNodes = 0;
        data = (ItemType[]) new Object[1000];
    }

    public boolean enque(ItemType newNode)
    {
        if(totalNodes == size) //overflow error -- returns false and breaks current maze search
            return false;
        else
        {
            data[rear] = newNode;
            rear = (rear + 1) % size;
            totalNodes++;
            return true;
        }
    }

    public ItemType deque()
    {
        if (totalNodes == 0)
            return null;
        else
        {
            int returnPosition = front;
            front = (front + 1) % size;
            totalNodes = totalNodes - 1;
            return (ItemType) data[returnPosition];
        }
    }

    public void showAll()
    {
        int k = front;
        for ( int i = 0; i < totalNodes; i++)
        {
            System.out.println(data[k]);
            k = (k + 1) % size;
        }
    }

    public int returnTotalNodes()
    {
        return totalNodes;
    }

}
