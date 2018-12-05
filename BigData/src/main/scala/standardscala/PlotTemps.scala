package standardscala

import scalafx.application
import standardscala.TempData.toDoubleOrNeg
import scalafx.application.JFXApp
import scalafx.collections.ObservableBuffer
import scalafx.scene.Scene
import scalafx.scene.chart.{NumberAxis, ScatterChart, XYChart}

import scala.io.Source

object PlotTemps extends JFXApp {
  val source = Source.fromFile("MN212142_9392.csv")
  val lines = source.getLines().drop(1)

  val data = lines.flatMap { line =>
    val p = line.split(",")
    if (p(7) == "." || p(8) == "." || p(9) == ".")
      Seq.empty
    else {
      Seq(TempData(p(0).toInt, p(1).toInt, p(2).toInt, p(4).toInt,
        toDoubleOrNeg(p(5)), toDoubleOrNeg(p(6)), p(7).toDouble, p(8).toDouble, p(9).toDouble))
    }
  }.toArray


  source.close()

  stage = new application.JFXApp.PrimaryStage {
    title = "Temp plot"
    scene = new Scene(500, 500) {
      val xAxis = NumberAxis()
      val yAxis = NumberAxis()
      val pData = XYChart.Series[Number, Number]("Temps",
        ObservableBuffer(data.map(td => XYChart.Data[Number, Number](td.doy, td.tmax)):_*))
      val plot = new ScatterChart(xAxis, yAxis, ObservableBuffer(pData))
      root = plot
    }
  }
}
