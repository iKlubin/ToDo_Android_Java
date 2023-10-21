package com.example.todo;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.textclassifier.TextClassifier;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Button dateText;
    private Button dateButton;
    private Calendar calendar;
    private LinearLayout lnrL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calendar = Calendar.getInstance();
        setTitle("TabHost");
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setContent(R.id.linearLayout);
        tabSpec.setIndicator("Дела");
        tabHost.addTab(tabSpec);
        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setContent(R.id.linearLayout2);
        tabSpec.setIndicator("Заметки");
        tabHost.addTab(tabSpec);
        tabHost.setCurrentTab(0);

        DateFormat dateFormat = new SimpleDateFormat("EEEE, dd/MM/yy");
        dateButton = findViewById(R.id.button);
        Calendar cal = Calendar.getInstance();
        dateText = findViewById(R.id.button);
        dateText.setText(dateFormat.format(cal.getTime()));
        lnrL = findViewById(R.id.lnrLayout);

        addNew("Отправить документы по почте до закрытия почтового отделения", true);
        addNew("Выполнить зарядку утром", false);
        addNew("Заказать продукты на неделю", true);
        addNew("Проверить почту и отвечать на важные письма", false);
        addNew("Убрать гараж и организовать инструменты", false);
    }

    public void addNew(String text, boolean isImportant){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.btn_todo, null);
        final Button button = view.findViewById(R.id.buttonToDo);
        int heightInDp = 100;
        if (text.length() > 25) {
            heightInDp = 150;
        }
        button.setText(text);
        float scale = getResources().getDisplayMetrics().density;
        int heightInPixels = (int) (heightInDp * scale + 0.5f);
        if (isImportant) {
            button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.monochrome_exclamationmark_triangle__1_, 0);
        } else {
            button.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightInPixels);
        button.setLayoutParams(layoutParams);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog(button);
            }
        });
        lnrL.addView(view);
    }

    public void showDialog(View v) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_layout, null);
        dialogBuilder.setView(dialogView);

        final EditText editText = dialogView.findViewById(R.id.editText);
        final CheckBox checkBox = dialogView.findViewById(R.id.checkBox);

        dialogBuilder.setTitle("Добавить задачу");
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String text = editText.getText().toString();
                boolean isImportant = checkBox.isChecked();
                addNew(text, isImportant);
            }
        });
        dialogBuilder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    public void showCustomDialog(final Button button) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Выберите действие")
                .setItems(new CharSequence[]{"Отметить как выполненое", "Удалить", "Отмена"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                button.setPaintFlags(button.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                button.setTextColor(Color.parseColor("#A081EE"));
                                break;
                            case 1:
                                ((LinearLayout) button.getParent()).removeView(button);
                                break;
                            case 2:
                                dialog.dismiss();
                                break;
                        }
                    }
                });
        builder.create().show();
    }

    public void showDatePickerDialog(View v) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        Toast.makeText(MainActivity.this, "Выбранная дата: " + selectedDate, Toast.LENGTH_SHORT).show();
                    }
                }, year, month, day);
        datePickerDialog.show();
    }
}