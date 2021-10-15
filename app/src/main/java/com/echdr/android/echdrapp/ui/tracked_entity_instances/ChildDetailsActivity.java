package com.echdr.android.echdrapp.ui.tracked_entity_instances;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.echdr.android.echdrapp.R;
import com.echdr.android.echdrapp.data.Sdk;
import com.echdr.android.echdrapp.data.service.ActivityStarter;
import com.echdr.android.echdrapp.ui.base.ListActivity;
import com.echdr.android.echdrapp.ui.enrollment_form.EnrollmentFormActivity;
import com.echdr.android.echdrapp.ui.events.EventsActivity;

import org.hisp.dhis.android.core.enrollment.Enrollment;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityInstance;

import java.util.List;

import io.reactivex.disposables.Disposable;


public class ChildDetailsActivity extends ListActivity {

    private String trackedEntityInstanceUid;
    private Disposable disposable;
    private TextView name;
    private TextView immuneNum;
    private TextView dob;
    private TextView gender;

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
        name = findViewById(R.id.name);
        immuneNum = findViewById(R.id.cd_no);
        dob = findViewById(R.id.cd_dob);
        gender = findViewById(R.id.cd_gender);

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
            immuneNum.setText(birthAndImmNum);

            String nameofChild = Sdk.d2().trackedEntityModule().trackedEntityAttributeValues()
                    .byTrackedEntityInstance().eq(trackedEntityInstanceUid)
                    .byTrackedEntityAttribute().eq("zh4hiarsSD5")
                    .one().blockingGet().value();
            name.setText(nameofChild);

            String dateofbirth = Sdk.d2().trackedEntityModule().trackedEntityAttributeValues()
                    .byTrackedEntityInstance().eq(trackedEntityInstanceUid)
                    .byTrackedEntityAttribute().eq("qNH202ChkV3")
                    .one().blockingGet().value();
            dob.setText(dateofbirth);

            String sex = Sdk.d2().trackedEntityModule().trackedEntityAttributeValues()
                    .byTrackedEntityInstance().eq(trackedEntityInstanceUid)
                    .byTrackedEntityAttribute().eq("lmtzQrlHMYF")
                    .one().blockingGet().value();
            gender.setText(sex);
        }catch (Exception e){
            e.printStackTrace();
        }


        getEnrollment();
        EnrollToPrograms();

    }

    @SuppressLint("WrongConstant")
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