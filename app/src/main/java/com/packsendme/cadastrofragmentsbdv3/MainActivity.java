/*
*
*
* ******IMPLEMENTAR enviaDadosToMapa() com Location API e Libraries avulsas nas dependencias
*
* *****IMPLEMENTAR ROOM + EXECUTOR PARA CADA OPERACAO*****
*
* exemplo no OnCreate()
*
* ---->//TEM QUE SER EM BACKGROUND SEM ASYNCTASK
        Executor tarefa = Executors.newSingleThreadExecutor();
        tarefa.execute(() -> {
            CadastroDatabase.getInstance(getBaseContext()).getCadastroDao().insert(new Cadastro(1, "Jonas", "Rua felumines, 77", "93544-8888"));
        });
*
* */

package com.packsendme.cadastrofragmentsbdv3;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int CALL_TO_SOMEONE = 1;
    FloatingActionButton fab,fabCadastrar,fabMostrar,fabLigar,fabLocalizar;
    //acao de cadastrar , acao de mostrar, de ligar e localizar

    TextView tvCadastrar;
    TextView tvMostrar;

    LinearLayout containerCad,containerMostrar,containerLigar,containerLocalizar;

    boolean fabIsOpen;
    public static final int TELA_MOSTRA_MAPA=1;


    com.packsendme.cadastrofragmentsbdv3.RegCad primeiroReg,ultimoReg,registro,aux;
    SQLiteDatabase db=null;//banco
    Cursor cursor;

    int colNome,colEndereco, colTelefone;//colunas

    int qtdRegistros, posicao=0;
   // public GoogleMap mMap;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        abreBanco();

        containerCad=findViewById(R.id.containerCad);
        containerMostrar=findViewById(R.id.containerMostrar);
        containerLigar=findViewById(R.id.containerLigar);
        containerLocalizar=findViewById(R.id.containerLocalizar);

        tvCadastrar=findViewById(R.id.tv_cadastrar);
        tvMostrar=findViewById(R.id.tv_mostrar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
               //         .setAction("Action", null).show();
                if(!fabIsOpen) {
                    showFabMenu();
                }else{
                    hideFabMenu();
                }
            }
        });

        fabCadastrar=(FloatingActionButton)findViewById(R.id.fab_cadastrar);
        fabCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//iniciaCadastro()
                //cadastra
  //              CadastroFragment cadastrinho=new CadastroFragment();
//                inicializaObjetosFrag(cadastrinho);
                try {
                        insereRegistro();
                        CadastroFragment cadastro = new CadastroFragment();
                        replaceFrag(cadastro,"Cadastro");
                        inicializaObjetosFrag(cadastro);
                        cadastro.campoTelefone.setText(null);
                        cadastro.campoNome.setText(null);
                        cadastro.campoEndereco.setText(null);
                        cadastro.campoNome.requestFocus();
                   // }else{
                     //   Toast.makeText(getBaseContext(),"Tente nào sair ou girar a tela\n \t\t da próxima vez, OK?",Toast.LENGTH_LONG).show();
                    //}
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        fabMostrar=(FloatingActionButton)findViewById(R.id.fab_mostrar);
        fabMostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mostra
                iniciaMostrarLista();
            }
        });

        fabLigar=(FloatingActionButton) findViewById(R.id.fab_ligar);
        fabLigar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mostraCxTexto("Liga","TESTE");

                if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    //sera que devemos mostrar uma explicacao ao user?
                    if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.CALL_PHONE)){
                        //mostra explicacao
                    }
                    return;
                }else {
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CALL_PHONE},CALL_TO_SOMEONE);//pode ser == 1
                }

                //mostraCxTexto("colTelefone depois: "+colTelefone,"TESTE");
                try {
                    Thread.sleep(8000);
                } catch (InterruptedException e) {
                    e.printStackTrace();

                }
                //if(cursor.moveToFirst()) {
                    startActivity(new Intent(Intent.ACTION_CALL,
                            Uri.parse("tel:" +
                                    cursor.getString(colTelefone))).
                            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                //}
            }
        });

        fabLocalizar=(FloatingActionButton) findViewById(R.id.fab_localizar);
        fabLocalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostraCxTexto("Algo deu errado com seu GPS\nChame a equipe de suporte e peca ajuda, OK?","Ops ;(");
                //enviaDadosToMapa();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction().add(R.id.fragment_content,new CadastroFragment(),"Cadastro");
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideFabMenu();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideFabMenu();
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideFabMenu();
    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            Bundle params=data.getExtras();
            if(params!=null){
                params.getString("colEndereco");
            }
        }
    }
*/
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("qtdRegistros",qtdRegistros);
        outState.putInt("posicao",posicao);

        //salvando o objeto
        outState.putParcelable("registro",registro);
        outState.putParcelable("primeiroReg",primeiroReg);
        outState.putParcelable("ultimoReg",ultimoReg);
        outState.putParcelable("aux",aux);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        qtdRegistros=savedInstanceState.getInt("qtdRegistros");
        posicao=savedInstanceState.getInt("posicao");

        //restaurando o objeto
        registro=savedInstanceState.getParcelable("registro");
        primeiroReg=savedInstanceState.getParcelable("primeiroReg");
        ultimoReg=savedInstanceState.getParcelable("ultimoReg");
        aux=savedInstanceState.getParcelable("aux");
    }
/*
    void salvaViews(View view){
        //MostraListaFragment mostraListaFragment=new MostraListaFragment();
        CadastroFragment cadastroFragment=new CadastroFragment();

        //mostraListaFragment.mostraNome.setText(cadastroFragment.campoNome.getText());
        cadastroFragment.campoNome.setText(registro.nome);
    }
*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        //fab
        if(!fabIsOpen) {
            showFabMenu();
        }else{
            hideFabMenu();
        }

        if(cursor!=null)//voltar
            cursor.close();
        else
            mostraCxTexto("TCHAU C:","Transicao");
        //iniciaApp
    }

    //ANimacao
    public void runAnimationFromXML(View view,int typeAndroidAnim){
        Animation animation= AnimationUtils.loadAnimation(this,typeAndroidAnim);
        fab.setAnimation(animation);
        view.startAnimation(animation);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sair) {
            hideFabMenu();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_cadastrar://tela de cadastro
                iniciaApp();
                hideFabMenu();
                abreBanco();
                //inicia cadastro
                break;
            case R.id.nav_mostrar:
               //MostraListaFragment mostra =new MostraListaFragment();
                hideFabMenu();
               fab=(FloatingActionButton)findViewById(R.id.fab);
               fab.animate().rotationY(0);
               //fab.hide();

                abreBanco();

                iniciaMostrarLista();
               //replaceFrag(mostra);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private void replaceFrag(Fragment fragment,String TAG) {
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.
                beginTransaction();
        //runAnimationFromXML(fab,android.R.anim.slide_in_left);
        transaction.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                transaction.replace(R.id.fragment_content,fragment,TAG);
        transaction.addToBackStack(null).commit();//faz funcionar o botao home
        fragmentManager.executePendingTransactions();//tem que por pra nao dar erro nas views
    }

    public void showFabMenu(){
        fabIsOpen=true;
        int dimension=R.dimen.fab_y_position1;
        FragmentManager fragmentManager=getSupportFragmentManager();

        List<Fragment> fragments=fragmentManager.getFragments();//lista todos os fragments
        Fragment visibleFragment=fragments.get(0);//fragment atual

//        runAnimationFromXML(fab,android.R.animator.fade_in);

       //ANimation
       // animation(containerCad,containerMostrar,containerLigar,containerLocalizar,0,1,0,1);


        //verificar em qual tela esta
        switch (visibleFragment.getTag()){
            case "MostraLista":
                //mostraCxTexto("Mostrar is visible ","TESTE");
                containerLocalizar.animate().alpha(1).setDuration(500);
                containerLigar.animate().alpha(1).setDuration(500);
                containerLigar.setVisibility(View.VISIBLE);
                containerLocalizar.setVisibility(View.VISIBLE);
                //ANimation
                containerLigar.animate().translationY(0).setDuration(500);
                containerLocalizar.animate().translationY(0).setDuration(500);
                break;
            case "Cadastro":
                //mostraCxTexto("Cadastro is visible ","TESTE");
                containerCad.animate().alpha(1).setDuration(500);
                containerMostrar.animate().alpha(1).setDuration(500);
                containerCad.setVisibility(View.VISIBLE);
                containerMostrar.setVisibility(View.VISIBLE);
                containerCad.animate().translationY(0).setDuration(500);
                containerMostrar.animate().translationY(0).setDuration(500);
                break;
                default:
                    mostraCxTexto("Ocorreu um erro :(\n entre em contato com o desenvolvedor. ","ERRO 0x17");break;
        }
       /* if(cadFrag.isVisible()) {

        }else if(mostrarFrag.isVisible()){

        }else{

        }*/
        fab.setImageResource(R.drawable.ic_menu_send);//muda o icone

    }

    public void hideFabMenu(){
        fabIsOpen=false;

        //ANIMATION AAQUI
        //animation(containerCad,containerMostrar,containerLigar,containerLocalizar,1,0,1,0);
        int valor=370;
        containerLigar.animate().translationY(valor).setDuration(500);
        containerLocalizar.animate().translationY(valor).setDuration(500);
        containerCad.animate().translationY(valor).setDuration(500);
        containerMostrar.animate().translationY(valor).setDuration(500);

        /*try {
            //containerCad.animate().alpha(0).setDuration(2500);
            //containerMostrar.animate().alpha(0).setDuration(2500);
            //containerLocalizar.animate().alpha(0).setDuration(2500);
            //containerLigar.animate().alpha(0).setDuration(2500);
            Thread.sleep(1990);
            containerCad.setVisibility(View.GONE);
            containerMostrar.setVisibility(View.GONE);
            containerLigar.setVisibility(View.GONE);
            containerLocalizar.setVisibility(View.GONE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        fab.setImageResource(R.drawable.baseline_add_white_48);
        /*tvCadastrar=findViewById(R.id.tv_cadastrar);
        tvMostrar=findViewById(R.id.tv_mostrar);

        tvCadastrar.animate().translationY(0);
        tvMostrar.animate().translationY(0);
        tvCadastrar.setVisibility(View.GONE);
        tvMostrar.setVisibility(View.GONE);

        fabCadastrar.animate().translationY(0);
        fabCadastrar.hide();
        fabMostrar.animate().translationY(0);
        fabMostrar.hide();*/
    }

    void inicializaObjetosFrag(CadastroFragment fragment){
        fragment.campoNome=findViewById(R.id.cadNome);
        fragment.campoTelefone=findViewById(R.id.cadTelefone);
        fragment.campoEndereco=findViewById(R.id.cadEndereco);
    }

    void inicializaObjetosFrag(MostraListaFragment fragment){
        fragment.mostraNome=findViewById(R.id.mostraNome);
        fragment.mostraTelefone=findViewById(R.id.mostraTelefone);
        fragment.mostraEndereco=findViewById(R.id.mostraEndereco);
        fragment.btnAnterior=findViewById(R.id.btnAnterior);
        fragment.btnProximo=findViewById(R.id.btnProximo);
    }

    public void iniciaApp() {
        CadastroFragment cad=new CadastroFragment();
        //fab.animate().rotationY(getResources().getDimension(R.dimen.fab_button_margin));//infelizmente deixa o botao manor
        fab.show();
        //cad.inicializaObjetos(); //
        //fechaBanco();

        replaceFrag(cad,"Cadastro");//tela principal
    }

    void iniciaMostrarLista(){
        hideFabMenu();//esconde
        //fab.hide();

        //verifica se existem regisros e muda para a tela de visualizacao
        if(!carregaRegistros()){//se retornar false eh pq deu errado
            mostraCxTexto("Nenhum registro cadastrado","Aviso");
            iniciaApp();
            return;
        }else{//senao
            /*
            posicao=1;
            aux=primeiroReg;
            Log.d(null,"AUXILIAR ANTES: "+aux);
            */
            MostraListaFragment aiou=new MostraListaFragment();
            replaceFrag( aiou,"MostraLista");
            mostraRegistro();

            aiou.btnProximo=findViewById(R.id.btnProximo);
            aiou.btnAnterior=findViewById(R.id.btnAnterior);

            aiou.btnAnterior.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getBaseContext(),"Anterior clicado",Toast.LENGTH_SHORT).show();
                    mostraRegAnterior();
                }
            });
            aiou.btnProximo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Toast.makeText(getBaseContext(),"Próximo clicado",Toast.LENGTH_SHORT).show();
                    mostraRegProximo();
                }
            });
        }
    }

    protected void mostraCxTexto(String msg, String titulo){
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(msg);
        builder.setNeutralButton("OK",null);
        AlertDialog dialog=builder.create();
        dialog.setTitle(titulo);
        dialog.show();
    }

    void mostraRegistro(){
        try {
            MostraListaFragment moistra = new MostraListaFragment();
            //moistra.getView().fi;
            //replaceFrag(moistra);
           // @Override
            inicializaObjetosFrag(moistra);

            //ANimacoes
            AlphaAnimation mScale=new AlphaAnimation(0,1);
            mScale.setDuration(500);
            mScale.setFillAfter(true);

            moistra.mostraNome.setAnimation(mScale);
            moistra.mostraEndereco.setAnimation(mScale);
            moistra.mostraTelefone.setAnimation(mScale);

            moistra.mostraNome.startAnimation(mScale);
            moistra.mostraEndereco.startAnimation(mScale);
            moistra.mostraTelefone.startAnimation(mScale);

            //if(cursor) {//sla

                moistra.mostraNome.setText("Nome: " + cursor.getString(colNome));
                //PEGA O QUE ESTA NA COLUNA NOME
                // E PASSA PRA STRING
                moistra.mostraTelefone.setText("Telefone: " + cursor.getString(colTelefone));
                moistra.mostraEndereco.setText("Endereco: " + cursor.getString(colEndereco));

                // mostraCxTexto("cursor.getString(colEndereco)\n\t---> "+cursor.getString(colEndereco),"CURSORES");

                //  Log.d(null, "\n\n\nendereco= " + moistra.mostraEndereco.getText());
            //}
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean carregaRegistros(){
        try{
            CadastroDbHelper helper=new CadastroDbHelper(getBaseContext());
            SQLiteDatabase db=helper.getReadableDatabase();

            String[] colunas={
                    BaseColumns._ID,
                    CadastroContract.CadastroEntry.COLUMN_NAME_NOME,
                    CadastroContract.CadastroEntry.COLUMN_NAME_ENDERECO,
                    CadastroContract.CadastroEntry.COLUMN_NAME_TELEFONE
            };

            cursor=db.query(CadastroContract.CadastroEntry.TABLE_NAME,colunas,//tabela , colunas
                    null,//WHERE
                    null,//args WHERE
                    null,
                    null,
                    null,
                    null);//sem LIMIT de registros retornados
            colEndereco=cursor.getColumnIndex(CadastroContract.CadastroEntry.COLUMN_NAME_ENDERECO);//retorna o numero da coluna(0 ou 1 ou 2) e grava no colEndereco
            //mostraCxTexto("get nome da tabela(aspas endereco aspas)\n\t--->"+cursor.getColumnName(cursor.getColumnIndex("endereco")),"CURSORES antes");
            colNome=cursor.getColumnIndex(CadastroContract.CadastroEntry.COLUMN_NAME_NOME);
            colTelefone=cursor.getColumnIndex(CadastroContract.CadastroEntry.COLUMN_NAME_TELEFONE);
         //   mostraCxTexto("colTelefone antes: "+colTelefone,"TESTE");

            //quando há dados na tabela pois checa se o resultado eh valido
            if (cursor!=null && cursor.moveToFirst() && cursor.getCount()!=0){
                cursor.moveToFirst();//posiciona o cursor no primeiro registro
                Log.d(null,"move to first: "+cursor.moveToFirst());
                return true;
            }else{
                return false;
            }

        }catch (Exception e){
            mostraCxTexto("Pesquisando BD... Nao foi possível achar dados no BD --->\n\t"+e.getMessage(),"ERRO 0x2DB");//erro load [data in] database
            return false;
        }
    }

    protected void insereRegistro(){
        //obj=Parcel.obtain();//sei la
        registro=new RegCad();//(obj)
        CadastroFragment cadastroFragment=new CadastroFragment();
//        registro.nome=registro.endereco=registro.telefone="";

        inicializaObjetosFrag(cadastroFragment);

        if (cadastroFragment.campoTelefone.getText().toString().isEmpty() ||
                cadastroFragment.campoNome.getText().toString().isEmpty() ||
                cadastroFragment.campoEndereco.getText().toString().isEmpty()) {//se deixou em branco
                    mostraCxTexto("Por favor, nao deixe nada em branco, para evitar problemas OK?\nDigite novamente.","Ops!   ;(");
        } else {

            try{
                CadastroDbHelper helper=new CadastroDbHelper(getBaseContext());
                SQLiteDatabase db=helper.getWritableDatabase();

                ContentValues values=new ContentValues();
                values.put(CadastroContract.CadastroEntry.COLUMN_NAME_NOME,cadastroFragment.campoNome.getText().toString());
                values.put(CadastroContract.CadastroEntry.COLUMN_NAME_ENDERECO,cadastroFragment.campoEndereco.getText().toString());
                values.put(CadastroContract.CadastroEntry.COLUMN_NAME_TELEFONE,cadastroFragment.campoTelefone.getText().toString());

                long novoIdRegistro=db.insert(CadastroContract.CadastroEntry.TABLE_NAME,null,values);
                //nao sera inserido quando nao tiver valores
                //Toast.makeText(getBaseContext(),"DEU CEROT INSERIU!",Toast.LENGTH_SHORT).show();
            }catch (Exception e){
               /* String strIsert="INSERT INTO listaclientes(nome,"+
                        "endereco,"+"telefone) "+
                        "VALUES ('"+
                        cadastroFragment.campoNome.getText().toString()+"',"+
                        cadastroFragment.campoEndereco.getText().toString()+"',"+
                        cadastroFragment.campoTelefone.getText().toString()+"')";

                strIsert="VALUES('8','8','3')";
                */

                mostraCxTexto("A inserir no BD... Nao foi possivel inserir os dados --->\n\t"+e.getMessage(),"ERRO 0x3DB");//erro insert into database
            }
            mostraCxTexto("Cliente cadastrado com sucesso!", "***AVISO***");
        }

//            inicializaObjetosFrag(cadastroFragment);
            registro.nome = cadastroFragment.campoNome.getText().toString();
            registro.endereco = cadastroFragment.campoEndereco.getText().toString();
            Log.d(null,"\n\nEndereco:  "+registro.endereco);
            registro.telefone = cadastroFragment.campoTelefone.getText().toString();

        //trata a ordem dos registros
        //colocar entre {}
        if(primeiroReg==null) {
            primeiroReg = registro;
            Log.d(null,"\n\nPrimeiro Registro "+primeiroReg);

        }
        registro.ant = ultimoReg;
        if(ultimoReg==null)
            ultimoReg=registro;
            //registro.prox=
        else{
            ultimoReg.prox=registro;
            ultimoReg=registro;
        }
        qtdRegistros++;
    }

    protected void mostraRegProximo(){
        //
        try{
            cursor.moveToNext();//posiciona o cursor para o prooximo registro
            mostraRegistro();
        }catch (Exception e){
            mostraCxTexto("Nao há mais registros de clientes no momento ","AVISO");
        }
    }

    protected void mostraRegAnterior(){
        //
        try{
            cursor.moveToPrevious();//posiciona o cursor para o registro anterior
            mostraRegistro();
        }catch (Exception e){
            mostraCxTexto("Nao há registros de clientes anteriores ","AVISO");
        }
    }

    public void abreBanco(){
        try{
            CadastroDbHelper helper=new CadastroDbHelper(getBaseContext());
            //Toast.makeText(getBaseContext(),"CRIOU O BD!",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            mostraCxTexto("Criando o banco de dados...Nao foi possivel abrir o BD! \n\t "+e.getMessage(),"ERRO 0x0CDB");//erro open or create database
        }
    }

   /*void enviaDadosToMapa(){
       Bundle params=new Bundle();
       //params.putString("colNome",cursor.getString(colNome));
       //if(cursor.moveToFirst()) {
//MUDAR em baixo
           params.putString("colEndereco", cursor.getString(colEndereco-1));
       //}
       //params.putString("colTelefone",cursor.getString(colTelefone));

       Intent intent=new Intent(MainActivity.this,MostraMapaActivity.class);
       intent.putExtras(params);

       startActivityForResult(intent,TELA_MOSTRA_MAPA);
   }*/
   
   public void animation(View layoutComponent1,View layoutComponent2,View layoutComponent3,View layoutComponent4,float fromX,float toX, float fromY, float toY){
       ScaleAnimation mScale=new ScaleAnimation(fromX,toX,fromY,toY);
       mScale.setDuration(500);
       mScale.setFillAfter(true);

       layoutComponent1.setAnimation(mScale);
       layoutComponent1.startAnimation(mScale);
       layoutComponent2.setAnimation(mScale);
       layoutComponent2.startAnimation(mScale);

       layoutComponent3.setAnimation(mScale);
       layoutComponent3.startAnimation(mScale);
       layoutComponent4.setAnimation(mScale);
       layoutComponent4.startAnimation(mScale);
   }
}
