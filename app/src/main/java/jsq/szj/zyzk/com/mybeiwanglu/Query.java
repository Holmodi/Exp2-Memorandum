package jsq.szj.zyzk.com.mybeiwanglu;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Query extends AppCompatActivity {

    private EditText et_content;
    private Button btn_query;
    private ListView listview;
    private SimpleAdapter simp_adapter;
    private List<Map<String, Object>> dataList;
    private NotesDB DB;
    private SQLiteDatabase dbread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        et_content = (EditText) findViewById(R.id.et_content);
        listview = (ListView) findViewById(R.id.listview);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        dataList = new ArrayList<Map<String, Object>>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        Button button = (Button)findViewById(R.id.buttonReturn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =
                        new Intent(Query.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        DB = new NotesDB(this);
        dbread = DB.getReadableDatabase();
        btn_query = (Button)findViewById(R.id.btn_query);
        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RefreshNotesList();
            }
        });
//        RefreshNotesList();

    }

    public void RefreshNotesList() {
        int size = dataList.size();
        listview = (ListView) findViewById(R.id.listview);
        if (size > 0) {
            dataList.removeAll(dataList);
            simp_adapter.notifyDataSetChanged();
            listview.setAdapter(simp_adapter);
        }
        simp_adapter = new SimpleAdapter(this, getData(), R.layout.item_jishiben_list,
                new String[]{"tv_content", "tv_date"}, new int[]{
                R.id.tv_content, R.id.tv_date});
        listview.setAdapter(simp_adapter);
    }

    private List<Map<String, Object>> getData() {
        et_content = (EditText) findViewById(R.id.et_content);
        String sql = "select * from note where content like ? order by content desc";
        Cursor cursor = dbread.rawQuery(sql,new String[]{"%"+et_content.getText()+"%"});
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("content"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("tv_content", name);
            map.put("tv_date", date);
            dataList.add(map);
        }
        cursor.close();
        return dataList;
    }
}
