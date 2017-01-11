package org.mydarties.dir;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.mydarties.R;
import org.mydarties.drawer.BaseActivity;
import org.mydarties.resultat.Product;

import java.util.ArrayList;

/**
 * Created by Guyader Vincent on 09/01/2017.
 */

public class SaisirProduct extends BaseActivity implements AdapterView.OnItemSelectedListener{

    private  ArrayList<Product> ProductArray;
    private Spinner SpinMag;
    public String[] Spin_item_categorie;
    private Button but_validate, but_cancel;
    private EditText margin_obj, margin_real, sales_obj, sales_real, turnover_obj, turnover_real;
    private int int_margin_obj, int_margin_real, int_sales_obj, int_sales_real, int_turnover_obj, int_turnover_real;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateDrawer(R.layout.activity_create_product_form);

        this.ProductArray = new ArrayList<>();
        this.ProductArray = (ArrayList<Product>) getIntent().getExtras().get("Array_Product");


        SpinMag = (Spinner)findViewById(R.id.spinner_nom_product);
        SpinMag.setOnItemSelectedListener(this);

        Spin_item_categorie = getResources().getStringArray(R.array.item_categorie);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Spin_item_categorie);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        SpinMag.setAdapter(dataAdapter);

        but_validate = (Button)findViewById(R.id.button_validate_add_product);
        but_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Indicatieur de completion
                int ok = 6;

                //Récupération des champs et vérification qu'ils ont été complétés
                margin_obj = (EditText)findViewById(R.id.edit_number_marge_obj);
                if(margin_obj.getText().toString().isEmpty()){ margin_obj.setError("Champs non remplie"); ok--; }

                margin_real  = (EditText)findViewById(R.id.edit_number_marge_real);
                if(margin_real.getText().toString().isEmpty()){ margin_real.setError("Champs non remplie"); ok--; }

                sales_obj = (EditText)findViewById(R.id.edit_number_vente_obj);
                if(sales_obj.getText().toString().isEmpty()){ sales_obj.setError("Champs non remplie"); ok--; }

                sales_real = (EditText)findViewById(R.id.edit_number_vente_real);
                if(sales_real.getText().toString().isEmpty()){ sales_real.setError("Champs non remplie"); ok--; }

                turnover_obj  = (EditText)findViewById(R.id.edit_number_turnover_obj);
                if(turnover_obj.getText().toString().isEmpty()){ turnover_obj.setError("Champs non remplie"); ok--; }

                turnover_real  = (EditText)findViewById(R.id.edit_number_turnover_real);
                if(turnover_real.getText().toString().isEmpty()){ turnover_real.setError("Champs non remplie"); ok--; }

                //Si tous les champs ont été remplis
                if(ok == 6){

                    int_margin_obj = Integer.parseInt(margin_obj.getText().toString());
                    int_margin_real = Integer.parseInt(margin_real.getText().toString());
                    int_sales_obj = Integer.parseInt(sales_obj.getText().toString());
                    int_sales_real = Integer.parseInt(sales_real.getText().toString());
                    int_turnover_obj = Integer.parseInt(turnover_obj.getText().toString());
                    int_turnover_real = Integer.parseInt(turnover_real.getText().toString());

                    boolean exist = false;

                    for(int i = 0; i < ProductArray.size(); i++){
                        if(ProductArray.get(i).getName().equals(SpinMag.getSelectedItem().toString())){
                            System.out.println(ProductArray.get(i).getName() + " == " + SpinMag.getSelectedItem().toString());
                            exist = true;
                        }
                    }


                    if(exist == false){

                        ProductArray.add(new Product(SpinMag.getSelectedItem().toString(), int_margin_obj, int_margin_real, int_sales_obj, int_sales_real, int_turnover_obj, int_turnover_real));

                        Intent addThing = new Intent(SaisirProduct.this, SaisirDir.class);

                        ArrayList<Product> Product_Array = ProductArray;

                        addThing.putExtra("Array_Product", Product_Array);

                        startActivity(addThing);
                    }else{
                        but_validate.setError("Categorie déjà saisie");
                        Toast.makeText(SaisirProduct.this, "Catégorie déjà saisie!", Toast.LENGTH_SHORT).show();
                    }


                }

            }
        });
        but_cancel = (Button)findViewById(R.id.button_validate_cancel_product);
        but_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent addThing = new Intent(SaisirProduct.this, SaisirDir.class);

                ArrayList<Product> Product_Array = ProductArray;
                addThing.putExtra("Array_Product", Product_Array);

                startActivity(addThing);

            }
        });



    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
