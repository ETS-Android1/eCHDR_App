package com.echdr.android.echdrapp.ui.tracked_entity_instances;

import static android.text.TextUtils.isEmpty;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.echdr.android.echdrapp.R;
import com.echdr.android.echdrapp.data.Sdk;
import com.echdr.android.echdrapp.data.service.ActivityStarter;
import com.echdr.android.echdrapp.data.service.forms.EventFormService;
import com.echdr.android.echdrapp.ui.base.ListActivity;
import com.echdr.android.echdrapp.ui.enrollment_form.EnrollmentFormActivity;
import com.echdr.android.echdrapp.ui.events.EventsActivity;

import org.hisp.dhis.android.core.enrollment.Enrollment;
import org.hisp.dhis.android.core.maintenance.D2Error;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityAttributeValueObjectRepository;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityDataValueObjectRepository;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityInstance;

import java.util.List;

import io.reactivex.disposables.Disposable;


public class ChildDetailsActivity extends ListActivity {

    private String trackedEntityInstanceUid;
    private Disposable disposable;
    private EditText name;
    private TextView cd_no;
    private TextView cd_dob;
    private TextView cd_gender;
    private EditText address;
    private EditText birthWeight;
    private EditText birthHeight;
    private Spinner ethnicity;
    private EditText GN_Area;
    private Spinner relationship;
    private EditText nic;
    private Spinner occupation;
    private Spinner sector;
    private Spinner highestEduLevel;
    private EditText mother_name;
    private EditText mother_dob;
    private EditText numberOfChildren;
    private EditText caregiver_name;
    private EditText lPhone;
    private EditText mNumber;


    private ImageView overweightNotEnrolled;
    private ImageView overweightEnrolled;
    private ImageView antopoNotEnrolled;
    private ImageView antopoEnrolled;
    private ImageView supplementaryNotEnrolled;
    private ImageView supplementaryEnrolled;
    private ImageView therapeuticNotEnrolled;
    private ImageView therapeuticEnrolled;
    private ImageView otherHealthNotEnrolled;
    private ImageView otherHealthEnrolled;
    private ImageView stuntingEnrolled;
    private ImageView stuntingNotEnrolled;
    private String orgUnit;

    public static int posOfItemSpinnerSelected;

    private enum IntentExtra {
        TRACKED_ENTITY_INSTANCE_UID
    }

    public static Intent getTrackedEntityInstancesActivityIntent(Context context, String uid) {
        Intent intent = new Intent(context, ChildDetailsActivity.class);
        intent.putExtra(IntentExtra.TRACKED_ENTITY_INSTANCE_UID.name(), uid);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_details);

        cd_no = findViewById(R.id.cd_no);
        cd_dob = findViewById(R.id.cd_dob);
        cd_gender = findViewById(R.id.cd_gender);
        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        birthWeight = findViewById(R.id.birthWeight);
        birthHeight = findViewById(R.id.birthWeight);
        ethnicity = findViewById(R.id.ethnicity);
        GN_Area = findViewById(R.id.GN_Area);
        relationship = findViewById(R.id.relationship);
        nic = findViewById(R.id.nic);
        occupation = findViewById(R.id.occupation);
        sector = findViewById(R.id.sector);
        highestEduLevel = findViewById(R.id.highestEduLevel);
        mother_name = findViewById(R.id.mother_name);
        mother_dob = findViewById(R.id.mother_dob);
        numberOfChildren = findViewById(R.id.numberOfChildren);
        caregiver_name = findViewById(R.id.caregiver_name);
        lPhone = findViewById(R.id.lPhone);
        mNumber = findViewById(R.id.mNumber);


        overweightNotEnrolled = findViewById(R.id.NotEnOverWeight);
        overweightEnrolled = findViewById(R.id.EnOverWeight);
        antopoNotEnrolled = findViewById(R.id.NotEnAntopo);
        antopoEnrolled = findViewById(R.id.EnAntopo);
        supplementaryNotEnrolled = findViewById(R.id.NotEnSupplementary);
        supplementaryEnrolled = findViewById(R.id.EnSupplementary);
        therapeuticNotEnrolled = findViewById(R.id.NotEnTera);
        therapeuticEnrolled = findViewById(R.id.EnTera);
        otherHealthNotEnrolled = findViewById(R.id.NotEnOtherHealth);
        otherHealthEnrolled = findViewById(R.id.EnOtherHealth);
        stuntingEnrolled = findViewById(R.id.EnStunting);
        stuntingNotEnrolled = findViewById(R.id.NotEnStunting);


        trackedEntityInstanceUid = getIntent().getStringExtra(IntentExtra.TRACKED_ENTITY_INSTANCE_UID.name());

        try{
            String birthAndImmNum = Sdk.d2().trackedEntityModule().trackedEntityAttributeValues()
                    .byTrackedEntityInstance().eq(trackedEntityInstanceUid)
                    .byTrackedEntityAttribute().eq("h2ATdtJguMq")
                    .one().blockingGet().value();
            cd_no.setText(birthAndImmNum);


            String dateofbirth = Sdk.d2().trackedEntityModule().trackedEntityAttributeValues()
                    .byTrackedEntityInstance().eq(trackedEntityInstanceUid)
                    .byTrackedEntityAttribute().eq("qNH202ChkV3")
                    .one().blockingGet().value();
            cd_dob.setText(dateofbirth);

            String sex = Sdk.d2().trackedEntityModule().trackedEntityAttributeValues()
                    .byTrackedEntityInstance().eq(trackedEntityInstanceUid)
                    .byTrackedEntityAttribute().eq("lmtzQrlHMYF")
                    .one().blockingGet().value();
            cd_gender.setText(sex);
        }catch (Exception e){
            e.printStackTrace();
        }


        getEnrollment();
        EnrollToPrograms();




    }

    private String getValueListener(String fieldUid, String dataElement) {
            TrackedEntityAttributeValueObjectRepository valueRepository =
                    Sdk.d2().trackedEntityModule().trackedEntityAttributeValues()
                            .value(
                                    fieldUid,
                                    dataElement

                            );
            String currentValue = valueRepository.blockingExists() ?
                    valueRepository.blockingGet().value() : "";

            return currentValue;
    }

    private void saveDataElement(String dataElement, String fieldUid){
        TrackedEntityDataValueObjectRepository valueRepository = null;
        try {
            valueRepository = Sdk.d2().trackedEntityModule().trackedEntityDataValues()
                    .value(
                            fieldUid,
                            dataElement
                    );
        }catch (Exception e)
        {

        }

        try{
            if(!isEmpty(dataElement))
            {
                valueRepository.blockingSet(dataElement);
            }else
            {
                valueRepository.blockingDeleteIfExist();
            }
        } catch (D2Error d2Error) {
            d2Error.printStackTrace();
        }
        
    }

    private void getEnrollment(){

        List<Enrollment> enrollments = Sdk.d2().enrollmentModule().enrollments()
                .byTrackedEntityInstance().eq(trackedEntityInstanceUid)
                .blockingGet();

        for (Enrollment v: enrollments) {
            System.out.println(v.program());
        }

        for (Enrollment v: enrollments) {

            if (v.program().equals("hM6Yt9FQL0n")) {
                antopoEnrolled.setVisibility(View.VISIBLE);
                antopoNotEnrolled.setVisibility(View.GONE);
            }

            if(v.program().equals("iUgzznPsePB")){
                otherHealthEnrolled.setVisibility(View.VISIBLE);
                otherHealthNotEnrolled.setVisibility(View.GONE);
            }

            if(v.program().equals("JsfNVX0hdq9")){
                overweightEnrolled.setVisibility(View.VISIBLE);
                overweightNotEnrolled.setVisibility(View.GONE);
            }

            if(v.program().equals("lSSNwBMiwrK")){
                stuntingEnrolled.setVisibility(View.VISIBLE);
                stuntingNotEnrolled.setVisibility(View.GONE);
            }

            if(v.program().equals("tc6RsYbgGzm")){
                supplementaryEnrolled.setVisibility(View.VISIBLE);
                supplementaryNotEnrolled.setVisibility(View.GONE);
            }

            if(v.program().equals("CoGsKgEG4O0")){
                therapeuticEnrolled.setVisibility(View.VISIBLE);
                therapeuticNotEnrolled.setVisibility(View.GONE);
            }

        }

    }

    private void EnrollToPrograms(){

        List<TrackedEntityInstance> s = Sdk.d2().trackedEntityModule().trackedEntityInstances().byUid().eq(trackedEntityInstanceUid).blockingGet();

        for (TrackedEntityInstance v: s) {
            orgUnit = v.organisationUnit();
            System.out.println("Organization Unit: " + orgUnit);
        }


        overweightNotEnrolled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityStarter.startActivity(ChildDetailsActivity.this,
                        EnrollmentFormActivity.getFormActivityIntent(getApplicationContext(), trackedEntityInstanceUid, "JsfNVX0hdq9", orgUnit), false);

            }
        });

        overweightEnrolled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = EventsActivity.getIntent(getApplicationContext(), "JsfNVX0hdq9", trackedEntityInstanceUid);
                startActivity(intent);
            }
        });

        antopoNotEnrolled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityStarter.startActivity(ChildDetailsActivity.this,
                        EnrollmentFormActivity.getFormActivityIntent(getApplicationContext(), trackedEntityInstanceUid, "hM6Yt9FQL0n", orgUnit), false);

            }
        });

        antopoEnrolled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = EventsActivity.getIntent(getApplicationContext(), "hM6Yt9FQL0n", trackedEntityInstanceUid);
                startActivity(intent);

            }
        });

        otherHealthNotEnrolled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityStarter.startActivity(ChildDetailsActivity.this,
                        EnrollmentFormActivity.getFormActivityIntent(getApplicationContext(), trackedEntityInstanceUid, "iUgzznPsePB", orgUnit), false);

            }
        });

        otherHealthEnrolled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = EventsActivity.getIntent(getApplicationContext(), "iUgzznPsePB", trackedEntityInstanceUid);
                startActivity(intent);

            }
        });

        stuntingNotEnrolled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityStarter.startActivity(ChildDetailsActivity.this,
                        EnrollmentFormActivity.getFormActivityIntent(getApplicationContext(), trackedEntityInstanceUid, "lSSNwBMiwrK", orgUnit), false);

            }
        });

        stuntingEnrolled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = EventsActivity.getIntent(getApplicationContext(), "lSSNwBMiwrK", trackedEntityInstanceUid);
                startActivity(intent);

            }
        });

        supplementaryNotEnrolled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityStarter.startActivity(ChildDetailsActivity.this,
                        EnrollmentFormActivity.getFormActivityIntent(getApplicationContext(), trackedEntityInstanceUid, "tc6RsYbgGzm", orgUnit), false);

            }
        });

        supplementaryEnrolled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = EventsActivity.getIntent(getApplicationContext(), "tc6RsYbgGzm", trackedEntityInstanceUid);
                startActivity(intent);

            }
        });

        therapeuticNotEnrolled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityStarter.startActivity(ChildDetailsActivity.this,
                        EnrollmentFormActivity.getFormActivityIntent(getApplicationContext(), trackedEntityInstanceUid, "CoGsKgEG4O0", orgUnit), false);

            }
        });

        therapeuticEnrolled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = EventsActivity.getIntent(getApplicationContext(), "CoGsKgEG4O0", trackedEntityInstanceUid);
                startActivity(intent);

            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }


}