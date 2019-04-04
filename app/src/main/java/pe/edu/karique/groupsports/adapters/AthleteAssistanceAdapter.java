package pe.edu.karique.groupsports.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.neo.stepbarview.StepBarView;
import pe.edu.karique.groupsports.R;
import pe.edu.karique.groupsports.models.Assistance;
import pe.edu.karique.groupsports.models.AssistanceShift;

/**
 * Created by karique on 3/05/2018.
 */

public class AthleteAssistanceAdapter extends RecyclerView.Adapter<AthleteAssistanceAdapter.AthleteViewHolder> {
    private List<Assistance> athletesAssistances;

    public AthleteAssistanceAdapter() {
    }

    public AthleteAssistanceAdapter(List<Assistance> athletesAssistances) {
        this.athletesAssistances = athletesAssistances;
    }

    @Override
    public AthleteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AthleteViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_athlete_assistance, parent, false));
    }

    @Override
    public void onBindViewHolder(final AthleteViewHolder holder, int position) {
        final Assistance athleteAssistance = athletesAssistances.get(position);
        athleteAssistance.setInitialShiftTurn();
        // TODO: Assign value to ImageView
        holder.nameTextView.setText(athleteAssistance.getFullName());
        holder.disciplineNameTextView.setText(athleteAssistance.getDisciplineName());
        holder.mesoscicloSeekBar.setMaxCount(athleteAssistance.getTotalMesocyclesInt() + 1);
        holder.mesoscicloSeekBar.setReachedStep(athleteAssistance.getCurrentMesocycleInt() + 1);
        holder.semanaSeekBar.setMaxCount(athleteAssistance.getTotalWeeksInt() + 1);
        holder.semanaSeekBar.setReachedStep(athleteAssistance.getCurrentWeekInt() + 1);
        updateShift(holder,athleteAssistance);

        holder.assistanceUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                athleteAssistance.setPrevShiftTurn();
                updateShift(holder,athleteAssistance);
            }
        });
        holder.assistanceCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                holder.cumplimientooSeekBar.setEnabled(b);

                //solo cuando se habilite el checkbox se guarda el progreso de entrenamiento
                if (b) {
                    if (athleteAssistance.getCurrentShift().isAttendance()){
                        Integer prog = athleteAssistance.getCurrentShift().getIntensityPercentage().intValue();
                        holder.cumplimientooSeekBar.setProgress(prog);
                        holder.cumplimientoTextView.setText(String.format("%s %%", String.valueOf(prog)));
                    }
                    else {
                        int progress = holder.cumplimientooSeekBar.getProgress();
                        athleteAssistance.setIntensityPercentageOfCurrentShift(progress);
                    }
                }
                athleteAssistance.setAttendanceOfCurrentShift(b);
            }
        });
        holder.assistanceDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                athleteAssistance.setNextShiftTurn();
                updateShift(holder,athleteAssistance);
            }
        });

        holder.cumplimientooSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                final AssistanceShift assistanceShift = athleteAssistance.getAssistanceShifts()
                                                                        .get(athleteAssistance.getShiftTurn());
                assistanceShift.setIntensityPercentage((double)progress);
                holder.cumplimientoTextView.setText(String.format("%d %%", progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Picasso.with(holder.itemView.getContext())
                .load(athleteAssistance.getPictureURL())
                .placeholder(R.drawable.athlete)
                .error(R.drawable.athlete)
                .into(holder.athleteImageView);
    }

    public void updateShift(final AthleteViewHolder holder, Assistance athleteAssistance){
        List<AssistanceShift> assistanceShifts = athleteAssistance.getAssistanceShifts();
        final AssistanceShift assistanceShift = assistanceShifts.get(athleteAssistance.getShiftTurn());
        holder.assistanceCheckBox.setText(AssistanceShift.getShiftNameFrom(assistanceShift));
        holder.assistanceCheckBox.setChecked(assistanceShift.isAttendance());
        int progress = assistanceShift.getIntensityPercentage() != null ? assistanceShift.getIntensityPercentage().intValue() : 50;
        holder.cumplimientooSeekBar.setProgress(progress);
        holder.cumplimientooSeekBar.setEnabled(assistanceShift.isAttendance());
        holder.cumplimientoTextView.setText(String.format("%s %%", String.valueOf(progress)));
    }

    @Override
    public int getItemCount() {
        return athletesAssistances.size();
    }

    public List<Assistance> getAthletesAssistances() {
        return athletesAssistances;
    }

    public void setAthletes(List<Assistance> athletesAssistances) {
        this.athletesAssistances = athletesAssistances;
    }

    class AthleteViewHolder extends RecyclerView.ViewHolder {
        CircleImageView athleteImageView;
        TextView nameTextView;
        TextView cumplimientoTextView;
        TextView disciplineNameTextView;
        SeekBar cumplimientooSeekBar;
        StepBarView mesoscicloSeekBar;
        StepBarView semanaSeekBar;
        ImageButton assistanceUpButton;
        CheckBox assistanceCheckBox;
        ImageButton assistanceDownButton;

        public AthleteViewHolder(View itemView) {
            super(itemView);
            athleteImageView = (CircleImageView) itemView.findViewById(R.id.athleteImageView);
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            disciplineNameTextView = (TextView) itemView.findViewById(R.id.disciplineNameTextView);
            cumplimientoTextView = (TextView) itemView.findViewById(R.id.cumplimientoTextView);
            cumplimientooSeekBar = (SeekBar) itemView.findViewById(R.id.cumplimientooSeekBar);
            mesoscicloSeekBar = (StepBarView) itemView.findViewById(R.id.mesoscicloSeekBar);
            semanaSeekBar = (StepBarView) itemView.findViewById(R.id.semanaSeekBar);
            assistanceUpButton = (ImageButton) itemView.findViewById(R.id.assistanceUpButton);
            assistanceCheckBox = (CheckBox) itemView.findViewById(R.id.assistanceCheckBox);
            assistanceDownButton = (ImageButton) itemView.findViewById(R.id.assistanceDownButton);
        }
    }
}
