To analyze a plate, a grid preset needs to be selected that contains information about the format of the plate. The relevant parameters are:

Name: A unique name for the preset.
dpi: The resolution of the image in dots per inch.
Rows: The number of rows of colonies on the plate.
Cols: The number of columns of coloniues on the plate.
dx: The mean horizontal distance in pixels between the centres of adjacent colonies.
dy: The mean vertical distance in pixels between the centres of adjacent colonies.

To view or edit the paramters for a preset, simply select the preset from the "Grid Presets" drop-down menu, and click edit. The following window will be displayed:

![https://balony.googlecode.com/svn/wiki/gridpresets-web.png](https://balony.googlecode.com/svn/wiki/gridpresets-web.png)

Pressing the "Calibrate" button will transfer the dx and dy values from the grid on the current image to the current preset.

The first time the a plate is analyzed with Balony, a window will appear asking the user to calibrate their imaging device. This enables suitable values for the dx and dy values to be obtained from a typical image and is recommended to maximize the accuracy of the auto-gridding process.