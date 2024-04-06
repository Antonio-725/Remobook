package com.example.onset;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link settings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class settings extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private CardView card1,card2,card3,card4,card5 ;
    FirebaseUser user;
    FirebaseAuth auth;

    public settings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment settings.
     */
    // TODO: Rename and change types and number of parameters
    public static settings newInstance(String param1, String param2) {
        settings fragment = new settings();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        auth=FirebaseAuth.getInstance();
        //card1=findViewById(R.id.card8);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Initialize card1 here
        card1 = view.findViewById(R.id.card8);
        card2=view.findViewById(R.id.card7);
        card3=view.findViewById(R.id.card4);
        card4=view.findViewById(R.id.card6);
        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a dialog with information about your app or company
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("About Us");
                builder.setMessage("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer eget risus ut nisl ultrices imperdiet. Nam ut dolor nec nisl tincidunt interdum. Integer nec arcu velit. Nullam consequat enim vel arcu ultricies, sed varius eros volutpat. Suspendisse potenti. Nulla facilisi. Vivamus et lorem sit amet odio feugiat tincidunt vel vel leo. In hac habitasse platea dictumst. Cras eu magna nec lacus accumsan cursus nec id elit. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Sed vel ipsum quis nisl placerat ullamcorper vel sed orci. Integer feugiat risus nisi, non commodo nulla ultricies a. Phasellus a massa vel purus tempor dapibus. Sed auctor eu elit ac feugiat. Integer bibendum lobortis venenatis.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Do nothing, simply close the dialog
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out this amazing app!");
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });

        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a dialog with privacy policies
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Privacy Policies")
                        .setMessage("1. Your personal information is collected only for the purpose of providing and improving the service. We do not use or share your information with anyone except as described in this Privacy Policy.\n\n" +
                                "2. We may use third-party services to monitor and analyze the use of our service.\n\n" +
                                "3. We may collect information that your browser sends whenever you visit our service. This information may include your computer's Internet Protocol (IP) address, browser type, browser version, the pages of our service that you visit, the time and date of your visit, the time spent on those pages, and other statistics.\n\n" +
                                "4. We may use cookies to collect information. You can instruct your browser to refuse all cookies or to indicate when a cookie is being sent. However, if you do not accept cookies, you may not be able to use some portions of our service.\n\n" +
                                "5. We may update our Privacy Policy from time to time. Thus, you are advised to review this page periodically for any changes. We will notify you of any changes by posting the new Privacy Policy on this page.\n\n" +
                                "6. Your continued use of the service after we post any modifications to the Privacy Policy on this page will constitute your acknowledgment of the modifications and your consent to abide and be bound by the modified Privacy Policy.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                // Show the dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        user=auth.getCurrentUser();
        if(user==null){
            Intent intent=new Intent(getContext(), LoginTenant.class);
            startActivity(intent);
            getActivity().finish();
        }
        else{
            card1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(getContext(), "You have Logged Out", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();

                }
            });

        }
        // Rest of your onCreateView method...

        return view;
    }


}