package com.example.toni.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.toni.myapplication.adapters.BoardAdapter;
import com.example.toni.myapplication.models.Board;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmMigration;
import io.realm.RealmResults;

public class BoardActivity extends AppCompatActivity implements RealmChangeListener<RealmResults<Board>>{

    private Realm realm;
    private RealmResults<Board> boards;

    private ListView listView;
    private List<Board> board;
    private BoardAdapter boardAdapter;
    private Button btnBorrar;
    private FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        //Conexion a la bd realm
        realm = Realm.getDefaultInstance();
        boards = realm.where(Board.class).findAll(); //consulta
        boards.addChangeListener(this);

        //--
        boardAdapter = new BoardAdapter(this, R.layout.list_view_board_item ,boards, realm);
        listView = (ListView)findViewById( R.id.listViewLista );
    //   Toast.makeText(getApplicationContext(), "Position: " + position, Toast.LENGTH_SHORT).show();


        this.listView.setAdapter(this.boardAdapter);

        //--
        floatingActionButton = (FloatingActionButton)findViewById(R.id.floatingActionButton);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   createNewBoard( "NUEVO TABLERO", new Date() );
                //   boardAdapter.notifyDataSetChanged(); // CAMBIO EN ADAPTADOR...
                showAddBoard("Agregar nuevo tablero", "Ingrese");

            }
        });



    }

    /*
     MENSAJE
     */
    public void showAddBoard( String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder( this);

        builder.setTitle(title);
        builder.setMessage(message);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_create_board, null);
        builder.setView(viewInflated);

        final EditText input = (EditText)viewInflated.findViewById(R.id.editText);
        builder.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String boardName = input.getText().toString().trim();

                if( boardName.length() > 0 ){
                    createNewBoard(boardName, new Date());
                    boardAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(getApplicationContext(), "se requiere un nombre: ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }


    /*
        SECCION CRUD
     */
    public  void createNewBoard( String boardName, Date date ){
        realm.beginTransaction();
        Board board = new Board( boardName, date );

        realm.copyToRealm( board);

        realm.commitTransaction();

    }

    @Override
    public void onChange(RealmResults<Board> element){
        Log.d("Tag", "Mensaje");
        boardAdapter.notifyDataSetChanged();
    }


    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main5, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch(menuItem.getItemId()){

            case R.id.eliminarTodo: {
                AlertDialog.Builder builder = new AlertDialog.Builder( this);

                builder.setTitle("Borrar todo");
                builder.setMessage("Esta seguro de borrar todo ?");

                View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_confirmation, null);
                builder.setView(viewInflated);

                Button cancelar = (Button)viewInflated.findViewById(R.id.butCancelar);
                Button borrarTodo = (Button)viewInflated.findViewById(R.id.borrarTodo);

                final AlertDialog dialog = builder.create();
                dialog.show();

                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.hide();
                    }
                });

                borrarTodo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       realm.beginTransaction();
                       realm.delete(Board.class);
                       realm.commitTransaction();

                       boardAdapter.notifyDataSetChanged();
                       dialog.hide();
                        Toast.makeText(getApplicationContext(), "Registros Eliminados", Toast.LENGTH_SHORT).show();
                    }
                });

                return true;

            }
            default:
                return super.onOptionsItemSelected(menuItem);
        }

    }
}
