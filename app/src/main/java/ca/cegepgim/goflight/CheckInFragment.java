package ca.cegepgim.goflight;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

import static com.paypal.android.sdk.payments.PaymentActivity.EXTRA_PAYMENT;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckInFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static PayPalConfiguration paypalconfig=new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .merchantName("Go Flight")
            .merchantPrivacyPolicyUri(Uri.parse("https://goflightinfo.000webhostapp.com/privacy-policy/"))
            .merchantUserAgreementUri(Uri.parse("https://goflightinfo.000webhostapp.com/privacy-policy/"))
            .clientId("AX8AoTzYqoQzwkrIe2A0pv-X1IEkP30C5DRZ5dPE9HuS7Zctn6rIdXk9u9nk6b5AmsEaGZYcXUELuZFE");

    private String mParam1;
    private String mParam2;
    EditText payingAmount;
    TextView paymentConfirmation;
    private Button payingNowButton;


    public CheckInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookFragment newInstance(String param1, String param2) {
        BookFragment fragment = new BookFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }*/


    private void payNow(){
        Intent intent=new Intent(getActivity(), PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,paypalconfig);
        getActivity().startService(intent);
        //PAYMENT_INTENT_SALE will cause the payment to complete immediately
        // change PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later
        //change PAYMENT_INTENT_ORDER to create a payment for
        PayPalPayment payPalPayment=new PayPalPayment(new BigDecimal(payingAmount.getText().toString()),
                "CAD","Akash Anaghan",PayPalPayment.PAYMENT_INTENT_ORDER);
        Intent intent1=new Intent(getActivity(), PaymentActivity.class);
        intent1.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,paypalconfig);
        intent1.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent1,0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK){
            PaymentConfirmation paymentConfirmation=data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if(paymentConfirmation!=null){
                try{
                    Log.i("Paypal response",paymentConfirmation.toJSONObject().toString(4));
                    this.paymentConfirmation.setText(paymentConfirmation.toJSONObject().toString(4));
                    //TODO: send 'confirmation to server for verification'
                }catch (JSONException e){
                    Log.e("Paypal JSONError:",e.toString());
                }
            }
        }else if(resultCode==Activity.RESULT_CANCELED){
            Log.i("Paypal ","User has canceled");
        }else if(resultCode==PaymentActivity.RESULT_EXTRAS_INVALID){
            Log.i("Paypal Example","An invalid Paypal Configuration");
        }
    }

    @Override
    public void onDestroy() {
        getActivity().stopService(new Intent(getActivity(),PayPalService.class));
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_checkin, container, false);
        payingAmount=view.findViewById(R.id.payingAmount);
        payingNowButton=view.findViewById(R.id.payNowButton);
        paymentConfirmation=view.findViewById(R.id.paymentConfirmation);
        payingNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payNow();
            }
        });
        return view;
    }
}
