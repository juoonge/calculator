package org.techtown.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.techtown.calculator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    boolean isFirstInput = true;
    boolean isOperatorClick=false; // 숫자누르고 바로 =을 누르는 경우 확인위한 변수
    double resultNumber = 0;
    double inputNumber = 0;
    String lastOperator="＋"; // =가 연속으로 눌렸을때를 위한 변수
    String operator = "＝"; // 연산자 저장 변수
    ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
    }

    public void numButtonClick(View view) {
        // Button button=findViewById(view.getId()); // 눌린 버튼의 객체 얻어온다.
        // String getButtonText=button.getText().toString();
        // tag는 findViewById() 사용하지 않고 View 상태에서 바로 정보 얻어올 수 있다.
        if (isFirstInput) {
            activityMainBinding.resultTextView.setText(view.getTag().toString());
            isFirstInput = false;
            if(operator.equals("＝")){
                activityMainBinding.mathTextView.setText(null); // mathtextview 초기화
                isOperatorClick=false;
            }
        } else {
            // 두번째부터 확인해서, 연속으로 0이 들어가지 않도록 함
            if (activityMainBinding.resultTextView.getText().toString().equals("0")) {
                // 0만 하나 들어있는 경우
                Toast.makeText(this, "0으로 시작되는 숫자는 없습니다.", Toast.LENGTH_LONG).show();
                // 새로 입력받음
                isFirstInput = true;
            } else {
                activityMainBinding.resultTextView.append(view.getTag().toString());
            }
        }
    }

    public void allClearButtonClick(View view) {
        activityMainBinding.resultTextView.setText("");
        activityMainBinding.mathTextView.setText("0");
        resultNumber = 0;
        operator = "＝";
        isFirstInput = true;
        isOperatorClick=false; //ex) 10 = = = = 눌렀을때 뒤에 =은 추가되지 않게함
    }

    public void pointButtonClick(View view) {
        // .을 처음 눌렀을때 0.이 되도록
        if (isFirstInput) {
            activityMainBinding.resultTextView.setText("0" + view.getTag().toString());
            isFirstInput = false;
        } else {
            // .이 여러개 찍히지 않도록
            if (activityMainBinding.resultTextView.getText().toString().contains("."))
                Toast.makeText(this, ".을 여러개 입력할 수 없습니다.", Toast.LENGTH_LONG).show();
            else
                activityMainBinding.resultTextView.append(view.getTag().toString());
        }
    }

    public void backspaceButtonClick(View view){
        // 계산이 다끝난 상태의 값에서는 적용되면 안됨
        if(!isFirstInput){
            String getResultText=activityMainBinding.resultTextView.getText().toString();
            if(getResultText.length()>1) {
                String subString = getResultText.substring(0, getResultText.length() - 1);
                activityMainBinding.resultTextView.setText(subString);
            }else{ // 모든걸 지웠을 때는 0을 표시
                activityMainBinding.resultTextView.setText("0");
                isFirstInput=true;
            }
        }
    }

    public void operatorClick(View view) {
        isOperatorClick=true;
        lastOperator=view.getTag().toString();
        if(isFirstInput){ // 1.=이 눌린후에 연산자가 눌린경우 2.연산자만 연속적으로 눌린경우(연산자 수정) -> 해당 연산자로 계속변경
            if(operator.equals("＝")){ // 식을 다 계산하고 그 수에 또 계산을 할때 위에 식이 아닌 결과값을 뜨게함
                operator=view.getTag().toString();
                resultNumber=Double.parseDouble(activityMainBinding.resultTextView.getText().toString());
                activityMainBinding.mathTextView.setText(resultNumber+ " " +operator+ " " );
            }else{
                operator=view.getTag().toString(); // 지금 눌린 operator
                String getMathText=activityMainBinding.mathTextView.getText().toString();
                String subString=getMathText.substring(0,getMathText.length()-2); // -2는 저장할때 넣은 공백 2개
                activityMainBinding.mathTextView.setText(subString);
                activityMainBinding.mathTextView.append(operator+" ");
            }
        }else {
            inputNumber = Double.parseDouble(activityMainBinding.resultTextView.getText().toString());

            resultNumber = calculator(resultNumber, inputNumber, operator);

            activityMainBinding.resultTextView.setText(String.valueOf(resultNumber));
            isFirstInput = true;
            operator = view.getTag().toString();
            activityMainBinding.mathTextView.append(inputNumber + " " + operator + " ");
        }
    }

    public void euqalsButtonClick(View view) {
        if(isFirstInput){ // =을 연속으로 누른경우
            if(isOperatorClick) {
                activityMainBinding.mathTextView.setText(resultNumber + " " + lastOperator + " " + inputNumber + " " + operator);
                resultNumber = calculator(resultNumber, inputNumber, lastOperator);
                activityMainBinding.resultTextView.setText(String.valueOf(resultNumber));
            }
        }else {
            inputNumber = Double.parseDouble(activityMainBinding.resultTextView.getText().toString());

            resultNumber = calculator(resultNumber, inputNumber, operator);
            activityMainBinding.resultTextView.setText(String.valueOf(resultNumber));
            isFirstInput = true;
            operator = view.getTag().toString();
            activityMainBinding.mathTextView.append(inputNumber + " " + operator + " ");
        }
    }

    private double calculator(double resultNumber, double inputNumber, String operator) { //Ctrl+Alt+m
        switch(operator) {
            case "＝":
                resultNumber=inputNumber;
                break;
            case "＋":
                resultNumber += inputNumber;
                break;
            case "－":
                resultNumber -= inputNumber;
                break;
            case "×":
                resultNumber *= inputNumber;
                break;
            case "÷":
                resultNumber /= inputNumber;
                break;
        }

        return resultNumber;
    }
}