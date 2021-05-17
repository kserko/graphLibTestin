package co.uk.graphlibrariestesting

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.utils.Easing
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import co.uk.graphlibrariestesting.databinding.FragmentFirstBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        multiLineChart()
        barChart()

    }

    private fun barChart() {
        val stackedBarData = listOf(
            BarEntry(1f, floatArrayOf(4f, 34f, 12f)),
            BarEntry(2f, floatArrayOf(5f, 20f, 9f)),
            BarEntry(3f, floatArrayOf(15f, 10f, 9f)),
            BarEntry(4f, floatArrayOf(6f, 8f, 19f)),
            BarEntry(5f, floatArrayOf(6f, 8f, 19f)),
            BarEntry(6f, floatArrayOf(16f, 18f, 19f)),
            BarEntry(7f, floatArrayOf(62f, 4f, 14f)),
            BarEntry(8f, floatArrayOf(15f, 10f, 9f)),
            BarEntry(9f, floatArrayOf(6f, 8f, 19f)),
            BarEntry(10f, floatArrayOf(6f, 3f, 19f)),
            BarEntry(11f, floatArrayOf(37f, 4f, 23f)),
            BarEntry(12f, floatArrayOf(37f, 8f, 23f))
        )

        val barChart: BarChart = binding.barChart.apply {
            description.isEnabled = false
            setFitBars(true)
            setTouchEnabled(true)
            setScaleEnabled(true)
            setExtraOffsets(5f, 5f, 5f, 10f)

            setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onValueSelected(e: Entry?, h: Highlight?) {
                    e?.let {
                        val barEntry =  (e as BarEntry)
                        val yVals = barEntry.yVals
                        val month = getMonthForValue(barEntry.x)
                        Log.i("Selected bar", "$month: Gas:${yVals[0]}, ElecDay:${yVals[1]}, ElecNight:${yVals[2]}")
                    }
                }

                override fun onNothingSelected() {

                }
            })

            this.xAxis.valueFormatter = monthValueFormatter()

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                setDrawAxisLine(true)

            }

            //yAxis
            getAxis(YAxis.AxisDependency.LEFT).apply {
                setDrawAxisLine(false)
                setDrawLabels(false)
                setDrawGridLines(false)
                axisMinimum = 0f
            }

            getAxis(YAxis.AxisDependency.RIGHT).apply {
                setDrawAxisLine(false)
                setDrawLabels(true)
                setDrawGridLines(false)
                axisMinimum = 0f
            }

            extraTopOffset = 0f

            legend.setup()

        }

        val singleBarData =
            listOf(BarEntry(1f, 5f), BarEntry(2f, 10f), BarEntry(3f, 15f), BarEntry(4f, 10f))


        val dataSet = BarDataSet(stackedBarData, "").apply {
            colors = mutableListOf(Color.RED, Color.LTGRAY, Color.BLACK)
            setDrawValues(false)
//            axisDependency = YAxis.AxisDependency.LEFT
            stackLabels = arrayOf("Gas", "Elec Day", "Elec Night")

        }

        val barData = BarData(dataSet)
        barChart.data = barData
        barChart.invalidate()
        barChart.animateXY(600, 600)
    }


    private fun multiLineChart() {
        val sampleDataOne = listOf(
            Entry(1f, 5f),
            Entry(2f, 10f),
            Entry(3f, 15f),
            Entry(4f, 10f),
            Entry(5f, 28f),
            Entry(6f, 3f),
            Entry(7f, 15f),
            Entry(8f, 49f)
        )
        val sampleDataTwo = listOf(
            Entry(1f, 2f),
            Entry(2f, 14f),
            Entry(3f, 10f),
            Entry(4f, 34f),
            Entry(5f, 12f),
            Entry(6f, 41f),
            Entry(7f, 10f),
            Entry(8f, 74f)
        )

        val sampleDataThree = listOf(
            Entry(1f, 12f),
            Entry(2f, 8f),
            Entry(3f, 22f),
            Entry(4f, 44f),
            Entry(5f, 11f),
            Entry(6f, 37f),
            Entry(7f, 11f),
            Entry(8f, 42f)
        )

        val lineChart: LineChart = binding.lineChart.apply {
            description.isEnabled = false
            legend.setup()
            setExtraOffsets(5f, 5f, 5f, 10f)
            setTouchEnabled(true)

            setOnChartValueSelectedListener(
                onLineChartValueSelectedListener(
                    listOf(
                        sampleDataOne,
                        sampleDataTwo,
                        sampleDataThree
                    )
                )
            )


            //xAxis
            xAxis.apply {
                valueFormatter = monthValueFormatter()
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
            }

            //yAxis
            getAxis(YAxis.AxisDependency.LEFT).apply {
                setDrawAxisLine(false)
                setDrawLabels(false)
                setDrawGridLines(true)
                granularity = 20f

            }

            getAxis(YAxis.AxisDependency.RIGHT).apply {
                setDrawAxisLine(false)
                setDrawLabels(true)
                setDrawGridLines(false)
            }

        }


        val dataSetOne = LineDataSet(sampleDataOne, "gas").apply {
            setup()
            circleColors = mutableListOf(Color.RED)
            colors = mutableListOf(Color.RED)
            fillColor = Color.RED
            fillAlpha = 10

        }
        val dataSetTwo = LineDataSet(sampleDataTwo, "elec day").apply {
            setup()
            circleColors = mutableListOf(Color.DKGRAY)
            colors = mutableListOf(Color.DKGRAY)
            fillColor = Color.DKGRAY
            fillAlpha = 10

        }

        val dataSetThree = LineDataSet(sampleDataThree, "elec night").apply {
            setup()
            circleColors = mutableListOf(Color.GREEN)
            colors = mutableListOf(Color.GREEN)
            fillColor = Color.GREEN
            fillAlpha = 10

        }


        val dateSets = listOf(dataSetOne, dataSetTwo, dataSetThree)
        val lineData = LineData(dateSets)
        lineChart.data = lineData
        lineChart.invalidate()

    }

    private fun onLineChartValueSelectedListener(dataSets: List<List<Entry>>) =
        object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                dataSets.forEachIndexed { index, dataSet ->
                    val x: Int? = e?.x?.toInt()
                    x?.let {
                        val entry = dataSet[x - 1]
                        val month = getMonthForValue(x.toFloat())
                        Log.i("ValueSelected", "month $month from dataset $index -> ${entry.y}")
                    }
                }

            }

            override fun onNothingSelected() {
                //
            }

        }

    private fun monthValueFormatter() = object : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return getMonthForValue(value)
        }
    }

    private fun getMonthForValue(value: Float) = when (value) {
        1f -> "Jan"
        2f -> "Feb"
        3f -> "Mar"
        4f -> "Apr"
        5f -> "May"
        6f -> "Jun"
        7f -> "Jul"
        8f -> "Aug"
        9f -> "Sep"
        10f -> "Oct"
        11f -> "Nov"
        12f -> "Dec"
        else -> ""
    }

    private fun Legend.setup() {
        verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        orientation = Legend.LegendOrientation.HORIZONTAL
        setDrawInside(false)
        formSize = 8f
        formToTextSpace = 4f
        xEntrySpace = 10f
    }

    private fun LineDataSet.setup() {
        axisDependency = YAxis.AxisDependency.LEFT
        setDrawCircles(false)
        setDrawValues(false)
        circleRadius = 8f
        setDrawFilled(true)
        fillAlpha = 80
        mode = LineDataSet.Mode.CUBIC_BEZIER
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}