package com.velen.whoplaysfirst.wheel;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.adefruandta.spinningwheel.SpinningWheelView;
import com.velen.whoplaysfirst.R;
import com.velen.whoplaysfirst.shared_prefs.SharedPrefsManager;
import com.velen.whoplaysfirst.viewPager.FragmentSelectionListener;

import java.util.ArrayList;
import java.util.List;

public class WheelFragment extends Fragment implements FragmentSelectionListener, PopupMenu.OnMenuItemClickListener , OptionsToDeleteSelectedListener {

    private SpinningWheelView wheel;
    private Button addBtn;
    private EditText editText;
    private TextView resultTxt;
    private ImageView refreshIcon, stopWheelImg, storeIcon;
    private ConstraintLayout layout;
    private MediaPlayer mediaPlayer;
    private List<String> myList = new ArrayList<>();
    private List<String> dummyItems = new ArrayList<>();
    private ListView retrieveOptionsList;
    private AlertDialog restoreDialog;

    public WheelFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wheel, container, false);

        wheel = view.findViewById(R.id.wheel);
        addBtn = view.findViewById(R.id.addBtn);
        storeIcon = view.findViewById(R.id.storeIcon);
        editText = view.findViewById(R.id.editText);
        resultTxt = view.findViewById(R.id.resultTxt);
        refreshIcon = view.findViewById(R.id.refreshIcon);
        stopWheelImg = view.findViewById(R.id.stopWheelImg);
        layout = view.findViewById(R.id.layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
            }
        });
        storeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOptionsUI(view);
            }
        });

        setupWheel();
        setupAddBtn();
        setupRefreshIcon();
        setUpEditText();

        return view;
    }

    private void setupWheel() {
        // Can be array string or list of object
        dummyItems = new ArrayList<>();
        dummyItems.add("Option 1");
        dummyItems.add("Option 2");
        dummyItems.add("Option 3");
        dummyItems.add("Option 4");
        dummyItems.add("Option 5");
        wheel.setWheelTextColor(Color.WHITE);
        wheel.setColors(R.array.wheel_colors);
        wheel.setItems(dummyItems);


        // Set listener for rotation event
        wheel.setOnRotationListener(new SpinningWheelView.OnRotationListener<String>() {
            // Call once when start rotation
            @Override
            public void onRotation() {
                Log.d("XXXX", "On Rotation");
                stopWheelImg.setVisibility(View.VISIBLE);
                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.wheel_sound);
                mediaPlayer.start();
                wheel.rotate(50, 6000, 50);
                hideResultText();
            }

            // Call once when stop rotation
            @Override
            public void onStopRotation(String item) {
                Log.d("XXXX", "On Rotation");
                stopWheelImg.setVisibility(View.GONE);
                mediaPlayer.release();
                showResultText(item);
            }
        });

        stopWheelImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wheel.rotate(50, 0, 50);
            }
        });

        // If true: user can rotate by touch
        // If false: user can not rotate by touch
        wheel.setEnabled(true);

    }

    private void setupAddBtn() {
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOption();
            }
        });
    }

    private void addOption() {
        String whatToAdd = editText.getText().toString();
        if(!whatToAdd.trim().equals("")) {
            myList.add(whatToAdd);
            wheel.setItems(myList);
            editText.setText("");
            hideKeyboard();
        } else {
            showKeyboard();
            editText.requestFocus();
        }
    }

    private void setOptions(List<String> options) {
        wheel.setItems(options);
    }

    private void setupRefreshIcon() {
        refreshIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myList = new ArrayList<>();
                wheel.setItems(dummyItems);
            }
        });
    }

    private void setUpEditText() {
        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    addOption();
                    return true;
                }
                return false;
            }
        });
    }

    private void hideKeyboard() {
        if(getActivity() != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if(imm != null) {
                if(getView() != null) {
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                }
            }
        }
    }

    private void showKeyboard() {
        if(getActivity() != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if(imm != null) {
                if(getView() != null) {
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                }
            }
        }
    }

    private void showResultText(String item) {
        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.text_scale);
        resultTxt.setText(item);
        resultTxt.setVisibility(View.VISIBLE);
        resultTxt.startAnimation(anim);
    }

    private void hideResultText() {
        resultTxt.setVisibility(View.GONE);
    }

    private void showOptionsUI(View view) {
        PopupMenu popup = new PopupMenu(getActivity(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.wheel_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }

    private void showStoreDialog() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        final EditText edittext = new EditText(getActivity());
        alert.setMessage("Enter a name for your selected options");
        alert.setTitle("Save current options");

        alert.setView(edittext);

        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        final AlertDialog dialog = alert.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String name = edittext.getText().toString();
                if (!myList.isEmpty()) {
                    if (name.length() < 1) {
                        edittext.setError("Enter a name to save your options");
                    } else {
                        if (SharedPrefsManager.getInstance().storeWheelOptions(getActivity(), name, myList)) {
                            dialog.dismiss();
                        } else {
                            edittext.setError("This name is already used");
                        }
                    }
                } else {
                    edittext.setError("No options selected to save");
                }
            }
        });
    }

    private void openRetrieveDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        List<String> names = SharedPrefsManager.getInstance().retrieveStoredWheelNames(getActivity());
        if(!names.isEmpty()) {
            dialogBuilder.setView(getRetrieveOptions(names));
        } else {
            dialogBuilder.setMessage("No saved options found");
        }

        dialogBuilder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        restoreDialog = dialogBuilder.show();
    }

    private View getRetrieveOptions(List<String> names) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.retrieve_options_container, null);
        retrieveOptionsList = view.findViewById(R.id.retrieveOptionsList);
        retrieveOptionsList.setAdapter(new RetrieveOptionsListAdapter(getActivity(), names, this));
        retrieveOptionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedName = (String)adapterView.getAdapter().getItem(position);
                List<String> options = SharedPrefsManager.getInstance().retrieveStoredWheelOptions(getActivity(), selectedName);
                setOptions(options);
                restoreDialog.dismiss();
                resultTxt.setVisibility(View.GONE);
            }
        });
        return view;
    }

    @Override
    public void onSelectedForDelete() {
        List<String> names = SharedPrefsManager.getInstance().retrieveStoredWheelNames(getActivity());
        retrieveOptionsList.setAdapter(new RetrieveOptionsListAdapter(getActivity(), names, this));
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveId:
                showStoreDialog();
                return true;
            case R.id.openId:
                openRetrieveDialog();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    @Override
    public void onFragmentUnselected() {
        if(mediaPlayer != null) {
            mediaPlayer.release();
        }
        resultTxt.setVisibility(View.GONE);
        ImageView icon = getActivity().findViewById(R.id.wheelIcon);
        icon.setSelected(false);
    }

    @Override
    public void onFragmentSelected() {

    }

}
