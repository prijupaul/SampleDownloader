package au.com.pactera.sampledownloader.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by priju.jacobpaul on 3/05/15.
 */
public class JSONResponse {
    private String title;
    private List<Rows> rows = new ArrayList<Rows>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Rows> getRows() {
        return rows;
    }

    public void setRows(List<Rows> Rows) {
        this.rows = Rows;
    }


    public String toString(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("Title: " + title + "\n");
        ListIterator<Rows> iterator = rows.listIterator();
        while(iterator.hasNext()){
            Rows row = iterator.next();
            buffer.append("Title: " + row.getTitle() + "\n");
            buffer.append("Description: " + row.getDescription() + "\n");
            buffer.append("imageUrl: " + row.getImageHref() + "\n");
        }
        return buffer.toString();
    }
}
