# Introduction #

The quantiation of colony sizes in Balony is a multi-step process. Essentially, the image is converted to monochrome, and colonies are identified as ellipses. Each colony is then mapped to a grid position and the size of the colony determined.

Varying degrees of automation are available depending on the quality of the imported images and how much control the user wishes to have. It is entirely possible that an entire experiment's worth of images can be analyzed with a single click.

The steps involved are:

  * [Thresholding](Thresholding.md) - conversion of the image from colour/grayscale to black and white.

  * [Gridding](Gridding.md) - determining the position of each colony within the array.

  * [Quantiation](Quantiation.md) - assigning pixel are values to each position.