import { useTheme } from "@emotion/react";
import { ResponsiveLine } from "@nivo/line";
import { tokens } from "../theme";

const LineChart = ({
  data ,
  tickSize = 15,
  min = "auto",
  max = "auto",
  curve = "step",
  xLedge,
  yLedge,
}) => {
  const theme = useTheme();
  const colors = tokens(theme.palette.mode);


  const tickPoints = () => {
    let arr = [];
    for (let i = 0; i < data[0].data.length; ++i) {
      let dp = data[0].data[i].x;
      if (parseInt(dp.substring(3)) % tickSize === 0) {
        arr.push(dp);
      }
    }
    return arr;
  };


  return (
    <ResponsiveLine
      data={data}
      theme={{
        axis: {
          domain: {
            line: {
              stroke: colors.grey[100],
            },
          },
          legend: {
            text: {
              fill: colors.grey[100],
            },
          },
          ticks: {
            line: {
              stroke: colors.grey[100],
              strokeWidth: 1,
            },
            text: {
              fill: colors.grey[100],
            },
          },
        },
        grid: {
          line: {
            stroke: colors.greenAccent[700],
            strokeWidth: 0.7,
          },
        },
        legends: {
          text: {
            fill: colors.grey[100],
          },
        },
        tooltip: {
          container: {
            color: colors.primary[500],
          },
        },
      }}
      colors={{ scheme: "paired" }} // added
      margin={{ top: 5, right: 110, bottom: 20, left: 60 }}
      xScale={{
        type: "point",
        min: "auto",
        max: "auto",
      }}
      yScale={{
        type: "linear",
        min: min,
        max: max,
        stacked: false,
        reverse: false,
      }}
      yFormat=" >-.2f"
      curve={curve}
      axisTop={null}
      axisRight={null}
      axisBottom={{
        orient: "middle",
        tickSize: 5,
        tickPadding: 5,
        tickRotation: 0,
        tickValues: tickPoints(),
      }}
      axisLeft={{
        orient: "left",
        tickValues: 10, // added
        tickSize: 1,
        tickPadding: 5,
        tickRotation: 0,
        legend: yLedge, // added
        legendOffset: -50,
        legendPosition: "middle",
      }}
      enableGridX={true}
      enableGridY={true}
      pointSize={8}
      borderColor={{ theme: 'background' }}
      pointColor={{ theme: "background" }}
      pointBorderWidth={2}
      pointBorderColor={{ from: "seriesColor" }}
      pointLabelYOffset={-12}
      useMesh={true}
      legends={[
        {
          anchor: "bottom-right",
          direction: "column",
          justify: false,
          translateX: 100,
          translateY: 0,
          itemsSpacing: 0,
          itemDirection: "left-to-right",
          itemWidth: 80,
          itemHeight: 10,
          itemOpacity: 0.75,
          symbolSize: 8,
          symbolShape: "circle",
          symbolBorderColor: "rgba(0, 0, 0, .5)",
          effects: [
            {
              on: "hover",
              style: {
                itemBackground: "rgba(0, 0, 0, .03)",
                itemOpacity: 1,
              },
            },
          ],
        },
      ]}
    />
  );
};

export default LineChart;
