package com.example.timestamper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Date;

public class NumPicDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.npdialog,null,false);

        final NumberPicker hrNumPicker = view.findViewById(R.id.hrNumPicker);
        hrNumPicker.setMaxValue(23);
        hrNumPicker.setMinValue(0);
        hrNumPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        view.findViewById(R.id.plus3HourButton).setOnClickListener(new ClickListener(
               hrNumPicker,3
        ));

        view.findViewById(R.id.minus3HourButton).setOnClickListener(new ClickListener(
                hrNumPicker,-3
        ));

        final NumberPicker minNumPicker = view.findViewById(R.id.minNumPicker);
        minNumPicker.setMaxValue(59);
        minNumPicker.setMinValue(0);
        minNumPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        view.findViewById(R.id.plus10MinButton).setOnClickListener(new ClickListener(
                minNumPicker, 10
        ));

        view.findViewById(R.id.minus10MinButton).setOnClickListener(new ClickListener(
                minNumPicker, -10
        ));

        final NumberPicker secNumPicker = view.findViewById(R.id.secNumPicker);
        secNumPicker.setMinValue(0);
        secNumPicker.setMaxValue(59);
        secNumPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        view.findViewById(R.id.plus10SecButton).setOnClickListener(new ClickListener(
                secNumPicker,10
        ));

        view.findViewById(R.id.minus10SecButton).setOnClickListener(new ClickListener(
                secNumPicker,-10
        ));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which){

                MainActivity mainActivity = (MainActivity) getActivity();

                //  NumberPickerで選択した時間分後のDateオブジェクトを作成する。
                long cdMilliSec = new Date().getTime();
                cdMilliSec += hrNumPicker.getValue() * 60 * 60 * 1000;
                cdMilliSec += minNumPicker.getValue() * 60 * 1000;
                cdMilliSec += secNumPicker.getValue() * 1000;

                TimeStamp countDownTimeStamp = new TimeStamp(new Date(),new Date(cdMilliSec));
                mainActivity.addTimeStamp(countDownTimeStamp);
            }
        });
        builder.setNegativeButton("cancel",null);
        builder.setView(view);

        return builder.create();
    }

    private void scroll(NumberPicker np,int y){
       np.scrollTo(0,3);
    }

    //  このクラス内でだけ必要なスクロール処理を行うためのクラス。
    //  イベントをセットするためにインターフェースを実装したクラスが必要になるため必須。
    private class ClickListener implements View.OnClickListener{

        private NumberPicker np;
        private int scrollDistance;

        public ClickListener(NumberPicker currentNumberPicker,int scrollDistance){
           this.np = currentNumberPicker;
           this.scrollDistance = scrollDistance;
        }

        @Override
        public void onClick(View v) {
            int changedPos = np.getValue() + scrollDistance;
            if(np.getMaxValue() < changedPos || np.getMinValue() > changedPos){
                np.setValue(0);
                return;
            }

            np.setValue( np.getValue() + scrollDistance );
        }
    }
}
