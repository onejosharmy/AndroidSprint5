package mapapp.cs4985.westga.edu.untitled;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class EntryAdapter extends ArrayAdapter<Entry> {

    private Context context;
    private List<Entry> list;

    public EntryAdapter(Context context, int resource, List<Entry> list) {
        super(context, resource, list);
        this.context = context;
        this.list = list;
    }
/**
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(this.context);
        View entryView = inflater.inflate(R.layout.entry_view, parent, false);
        Entry entry = this.list.get(position);

        TextView text = entryView.findViewById(R.id.textviewname);
        text.setText(entry.getName() + "         " + entry.getTemperature() + "\u00b0" + "F");
        TextView shortforecast = entryView.findViewById(R.id.textviewsize);
        shortforecast.setText(entry.getShortForecast());

        ImageView img = entryView.findViewById(R.id.button);
        try {
            img.setImageDrawable(this.loadImageFromURL(entry.getImageResource()));
        } catch (Exception e) {
            e.getStackTrace();
        }
        return entryView;
    }**/

    public Drawable loadImageFromURL(String url) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);
        try {
            InputStream stream = (InputStream) new URL(url).getContent();
            Drawable drawable = Drawable.createFromStream(stream, "icon");
            return drawable;
        } catch (Exception e) {
            return null;
        }
    }
}

