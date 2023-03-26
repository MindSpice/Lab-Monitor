import { ResponsiveBar } from "@nivo/bar";
import { tokens } from "../theme";
import { useTheme } from "@emotion/react";

const BarChart = ({
  data = [],
  barColors,
  keys,
  margin,
  groupMode = "stacked",
  indexBy,
  maxValue = "auto",
  ledgeX = "",
  ledgeY = "",
}) => {
  const theme = useTheme();
  const colors = tokens(theme.palette.mode);

  return (
    <ResponsiveBar
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
      data={data}
      keys={keys}
      indexBy={indexBy}
      margin={margin}
      groupMode={groupMode}
      padding={0.3}
      valueScale={{ type: "linear" }}
      indexScale={{ type: "band", round: true }}
      maxValue={maxValue}
      colors={barColors}
      defs={[
        {
          id: "dots",
          type: "patternDots",
          background: "inherit",
          color: "#38bcb2",
          size: 4,
          padding: 1,
          stagger: true,
        },
        {
          id: "lines",
          type: "patternLines",
          background: "inherit",
          color: "#eed312",
          rotation: -45,
          lineWidth: 6,
          spacing: 10,
        },
      ]}
      borderColor={{
        from: "color",
        modifiers: [["darker", 1.6]],
      }}
      axisRight={null}
      // axisTop={{
      //   tickSize: 2,
      //   tickPadding: 5,
      //   tickRotation: 10,
      //   orient: "bottom",
      //   legend: ledgeX,
      //   legendPosition: "middle",
      //   legendOffset: 32,
      // }}
      // axisLeft={{
      //   tickSize: 5,
      //   tickPadding: 5,
      //   tickRotation: 0,
      //   legend: ledgeY,
      //   legendPosition: "middle",
      //   legendOffset: -32,
      // }}

      axisBottom={{
        tickSize: 5,
        tickPadding: 5,
        tickRotation: 0,
        legend: ledgeX,
        legendPosition: "middle",
        legendOffset: 32,
      }}
      axisLeft={{
        tickSize: 5,
        tickPadding: 5,
        tickRotation: 0,
        legend: ledgeY,
        legendPosition: "middle",
        legendOffset: -40,
      }}
      labelSkipWidth={12}
      labelSkipHeight={12}
      labelTextColor={{
        from: "color",
        modifiers: [["darker", 1.6]],
      }}
      legends={[
        {
          dataFrom: "keys",
          anchor: "bottom-right",
          direction: "column",
          justify: false,
          translateX: 120,
          translateY: 0,
          itemsSpacing: 2,
          itemWidth: 100,
          itemHeight: 20,
          itemDirection: "left-to-right",
          itemOpacity: 0.85,
          symbolSize: 20,
          effects: [
            {
              on: "hover",
              style: {
                itemOpacity: 1,
              },
            },
          ],
        },
      ]}
      role="application"
      ariaLabel="Disk Usage"
      barAriaLabel={function (e) {
        return (
          e.id + ": " + e.formattedValue + " On mount: " + e.indexValue + "GB"
        );
      }}
    />
  );
};

export default BarChart;
