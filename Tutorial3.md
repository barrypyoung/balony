[< Prev](Tutorial2.md) [Next >](Tutorial4.md)

  1. Click on the "Image" tab to go to the image analysis section.
  1. The file list should contain the individual images. If not, click "Chose Folder..." and navigate to the folder containing the individual images. This should be a folder called "Cropped" under the folder where the original image files were extracted.
  1. Select "Loewen Lab 1536 RePad 300 dpi" from the "Grid Presets" drop-down menu.
  1. _De-select_ the "Threshold", "Grid", "Quant" and "Save" checkboxes in order to see the separate stages of analysis:<p><img src='https://balony.googlecode.com/svn/wiki/autosettings-tut.png' /></p>
  1. Double-click the first file in the file list. It should open to the right.
  1. If a window pops up asking you to calibrate your imaging device, simply click "Use existing preset" and then select "Loewen Lab 1536 RePad 300 dpi" from the drop-down menu and click "Use this preset".
  1. To threshold the image, make sure "Auto" is selected in the "Threshold Method" panel, then click the "Threshold" button near the top of the window. To see the effects of different threshold values, try selecting "Manual" then user the slider to change the threshold value and click the "Threshold" button again.
  1. After using the automatic thresholding, click "AutoGrid". A green grid should be superimposed over the image.<p><img src='https://balony.googlecode.com/svn/wiki/gridded-tut.png' /></p>
  1. Click the "Quantify" button. After a brief pause, the program should draw green circles around each colony on the plate.<br><br><img src='https://balony.googlecode.com/svn/wiki/image-quant-web.png' /><br><br>
<ol><li>Click "Save" to save the quantified image.<br>
</li><li>Click "Show Quant" on the left to see the raw quantified pixel data for each position in the array.<br>
</li><li>Now process the remaining images:<br>
</li><li>Make sure the "Threshold", "Grid", "Quant" and "Save" checkboxes are selected, as below.<br><br><img src='https://balony.googlecode.com/svn/wiki/image-web.png' /><br><br>
</li><li>Click "Select All" followed by "Analyze Selected".<br>
</li><li>One of the files might require a second pass to be analyzed correctly (<tt>SCS2_G418_set-1_plate-3.jpg</tt>). You can click "Yes" to the dialog that asks about doing a second pass.