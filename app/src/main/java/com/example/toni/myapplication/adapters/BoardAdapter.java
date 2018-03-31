package com.example.toni.myapplication.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toni.myapplication.R;
import com.example.toni.myapplication.models.Board;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmModel;

/**
 * Created by toni on 24/03/2018.
 */

public class BoardAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Board> list;
    private Realm realm;

    public BoardAdapter(Context context, int layout, List<Board> list, Realm realm) {
        this.realm = realm;
        this.context = context;
        this.layout = layout;
        this.list = list;
    }



    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewInterface vh;
        final int idBorrar;
        if( convertView == null){
            //Se crea la vista y se le asigna a convertView, asi ya tenemos nuestra vista de layout con componentes visuales.

            convertView = LayoutInflater.from(context).inflate(layout, null);
            vh = new ViewInterface();

            vh.btnBorrar = (Button) convertView.findViewById(R.id.btnBorrar);
            vh.title = (TextView) convertView.findViewById(R.id.title);
            vh.createdAt = (TextView) convertView.findViewById(R.id.createdAt);


            convertView.setTag(vh);

        }else{
            vh = (ViewInterface) convertView.getTag();
         }

        final Board board = list.get(position);
        idBorrar = board.getId();

        vh.title.setText(board.getTitle());
        vh.createdAt.setText( board.getCreatedAt().toString());

        vh.btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Board board = realm.where(Board.class).equalTo("id", titleBorrar).findFirst();
                Toast.makeText(context, "{ id: " +  board.getId() + ", title: " + board.getTitle() + ", date: "+ board.getCreatedAt() + " }", Toast.LENGTH_SHORT).show();

                realm.beginTransaction();
                 board.deleteFromRealm();
                realm.commitTransaction();

            }
        });


        vh.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Board board = realm.where(Board.class).equalTo("id", titleBorrar).findFirst();
                AlertDialog.Builder builder = new AlertDialog.Builder( context);

                builder.setTitle("Modificar");
                builder.setMessage("Actualice su informacion");

                View viewInflated = LayoutInflater.from(context).inflate(R.layout.dialog_create_board, null);
                builder.setView(viewInflated);

                final EditText input = (EditText)viewInflated.findViewById(R.id.editText);
                input.setText(board.getTitle());

                builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String boardName = input.getText().toString().trim();

                        if( boardName.length() > 0 ){
                            //  Board boardTmp = realm.where(Board.class).equalTo("id", idBorrar).findFirst();

                            Board boardTmp = new Board();

                            boardTmp.setCreatedAt(new Date() );
                            boardTmp.setTitle( boardName );
                            boardTmp.setId(board.getId());

                            realm.beginTransaction();
                            realm.copyToRealmOrUpdate(boardTmp);
                            realm.commitTransaction();
                        }
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });


        return convertView;
    }



    public void borralElemento(int position, View convertView){
        ViewInterface vh;

        vh = new ViewInterface();

        vh.btnBorrar = (Button) convertView.findViewById(R.id.btnBorrar);

        vh.btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "BORRARA EL REGISTRO", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public class ViewInterface{
      //  TextView id;
        TextView title;
        TextView createdAt;
        Button btnBorrar;
    }
}
