package com.example.gbkim.gubonny.Common;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.example.gbkim.gubonny.Common.Decorators.OneDayDecorator;
import com.example.gbkim.gubonny.R;
import com.example.gbkim.gubonny.util.Utils;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Dialog_Calendar2 extends Dialog{
    private Context m_Context;

    @BindView(R.id.iv_dialog_calendar_type2_close)
    ImageView imageTopCloseButton;

    @BindView(R.id.tv_dialog_calendar_type2_today_button)
    TextView textTodayButton;
    @BindView(R.id.tv_dialog_calendar_type2_title)
    TextView textTopTitle;
    @BindView(R.id.tv_dialog_calendar_type2_date_start_date)
    TextView textStartDate;
    @BindView(R.id.tv_dialog_calendar_type2_date_end_date)
    TextView textEndDate;

    @BindView(R.id.v_dialog_calendar_type2_date_start_indicator)
    View viewStartDateIndicator;
    @BindView(R.id.v_dialog_calendar_type2_date_end_indicator)
    View viewEndDateIndicator;

    @BindView(R.id.cv_dialog_calendar_type2)
    MaterialCalendarView calendarView;

    @BindView(R.id.btn_dialog_calendar_type2_ok)
    Button buttonBottomOk;

    private OnClickListener m_OnClickListener;

    private String sTitle;
    private String getTime;
    private boolean bFirstSelect = true;

    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();

    public interface OnClickListener {
        void OnOkClickListener(String previousDate, String forwardDate);

        void OnOkClickListener(String todayDate);
    }

    public void setOnClickListener(OnClickListener listener) {
        m_OnClickListener = listener;
    }

    public Dialog_Calendar2(@NonNull Context context, String title) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        this.m_Context = context;
        this.sTitle = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 다이얼로그 레이아웃 설정
        setContentView(R.layout.dialog_calendar_type2);

        ButterKnife.bind(this);

        init();

        UIEvents();
    }

    private void init() {
        textTopTitle.setText(sTitle);

        try {
            setCurrentTime();

        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    private Date getCurrentDate() {
        // Step1. 현재 시간 가져오기.
        long now = System.currentTimeMillis();
        // Step2. Date 생성하기
        return new Date(now);
    }

    @NonNull
    private String getConvertDate(Date date) {
        // Step3. 가져오고 싶은 형식으로 가져오기
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        // 출처: https://liveonthekeyboard.tistory.com/entry/안드로이드-현재-시간-현재-날짜-구하기-SimpleDateFormat [키위남]
        getTime = sdf.format(date);

        return getTime;
    }

    private void UIEvents() {
        calendarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDay a = calendarView.getSelectedDate();

                // "yyy-MM-dd" 형식으로 변경
                String selectDate = String.format(Locale.KOREA, "%1$tY-%1$tm-%1$td", a);

                // 시작 일
                if (bFirstSelect) {
                    textStartDate.setText(selectDate);
                    viewStartDateIndicator.setBackgroundColor(
                            Utils.getResourceColor(
                                    m_Context,
                                    R.color.calendarDialogCalendarHeaderPeriodIndicatorSelectColor
                            )
                    );
                    viewEndDateIndicator.setBackgroundColor(
                            Utils.getResourceColor(
                                    m_Context,
                                    R.color.calendarDialogCalendarHeaderPeriodIndicatorDefaultColor
                            )
                    );

                    bFirstSelect = false;

                    // 종료 일
                } else {
                    String sPrevious = textStartDate.getText().toString();
                    boolean bChange = Utils.DateCompare(sPrevious, selectDate);

                    if (bChange) {
                        textEndDate.setText(selectDate);

                    } else {
                        textStartDate.setText(selectDate);
                        textEndDate.setText(sPrevious);
                    }

                    viewEndDateIndicator.setBackgroundColor(
                            Utils.getResourceColor(
                                    m_Context,
                                    R.color.calendarDialogCalendarHeaderPeriodIndicatorSelectColor
                            )
                    );

                    bFirstSelect = true;
                }

                setBottomButtonEnabled();
            }
        });

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                // "yyy-MM-dd" 형식으로 변경
                String selectDate = Utils.convertDateFormatyyyy_MM_dd(date);

                textStartDate.setText(selectDate);
                viewStartDateIndicator.setBackgroundColor(
                        Utils.getResourceColor(
                                m_Context,
                                R.color.calendarDialogCalendarHeaderPeriodIndicatorSelectColor
                        )
                );
                viewEndDateIndicator.setBackgroundColor(
                        Utils.getResourceColor(
                                m_Context,
                                R.color.calendarDialogCalendarHeaderPeriodIndicatorDefaultColor
                        )
                );

                setBottomButtonEnabled();

                calendarView.addDecorator(new TodayDecorator(true));
            }
        });

        // 기간 선택 이벤트
        calendarView.setOnRangeSelectedListener((widget, dates) -> {
            String sPrevious = Utils.convertDateFormatyyyy_MM_dd(dates.get(0));
            String selectDate = Utils.convertDateFormatyyyy_MM_dd(dates.get(dates.size() - 1));

            textStartDate.setText(sPrevious);
            textEndDate.setText(selectDate);

//            boolean bChange = Utils.DateCompare(sPrevious, selectDate);

//            if (bChange) {
//                textEndDate.setText(selectDate);
//
//            } else {
//                textStartDate.setText(selectDate);
//                textEndDate.setText(sPrevious);
//            }

            viewEndDateIndicator.setBackgroundColor(
                    Utils.getResourceColor(
                            m_Context,
                            R.color.calendarDialogCalendarHeaderPeriodIndicatorSelectColor
                    )
            );

            setBottomButtonEnabled();
        });

        imageTopCloseButton.setOnClickListener(view -> dismiss());

        buttonBottomOk.setOnClickListener(view -> {
            dismiss();

            if (m_OnClickListener != null) {
                String sPrevious = textStartDate.getText().toString();
                String sForward = textEndDate.getText().toString();

                if (sPrevious.equals(sForward)) {
                    m_OnClickListener.OnOkClickListener(sForward);

                } else {
                    m_OnClickListener.OnOkClickListener(sPrevious, sForward);
                }
            }
        });

        // 오늘 날짜 버튼 클릭
        textTodayButton.setOnClickListener(view -> {
            try {
                setCurrentTime();

            } catch (OutOfDateRangeException e) {
                e.printStackTrace();
            }
        });
    }

    private void setCurrentTime() throws OutOfDateRangeException {
        Date date = getCurrentDate();
        String getTime = getConvertDate(date);

        // 이전 선택된 날짜 초기화
        calendarView.clearSelection();
        // 오늘 날짜만 선택하기
        calendarView.addDecorator(
                new TodayDecorator(false)
        );

        textStartDate.setText(getTime);
        textEndDate.setText(getTime);


        List<Calendar> calendars = new ArrayList<>();
//        calendarView.setSelectedDates(calendars);

        viewStartDateIndicator.setBackgroundColor(
                Utils.getResourceColor(
                        m_Context,
                        R.color.calendarDialogCalendarHeaderPeriodIndicatorSelectColor
                )
        );
        viewEndDateIndicator.setBackgroundColor(
                Utils.getResourceColor(
                        m_Context,
                        R.color.calendarDialogCalendarHeaderPeriodIndicatorSelectColor
                )
        );

        setBottomButtonEnabled();
    }

    private void setBottomButtonEnabled() {
        Drawable background = viewEndDateIndicator.getBackground();

        if (background instanceof ColorDrawable) {
            int color = ((ColorDrawable) background).getColor();

            if (color == Utils.getResourceColor(m_Context, R.color.calendarDialogCalendarHeaderPeriodIndicatorSelectColor)) {
                buttonBottomOk.setEnabled(true);

            } else {
                buttonBottomOk.setEnabled(false);
            }
        }
    }

    private class TodayDecorator implements DayViewDecorator {

        private final CalendarDay today;
        private final Drawable backgroundDrawable;

        public TodayDecorator(boolean bSelected) {
            today = CalendarDay.today();

            if (bSelected) {
                backgroundDrawable = Utils.getDrawable(
                        m_Context,
                        R.drawable.today_circle_backgorund_transparency
                );

            } else {
                backgroundDrawable = Utils.getDrawable(
                        m_Context,
                        R.drawable.today_circle_backgorund
                );
            }
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return today.equals(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setBackgroundDrawable(backgroundDrawable);
        }
    }
}
