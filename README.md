# StrategyGameUtils

A set of utilities useful when building a 2d strategy game with libgdx

## Status and motiviation

This project in a working in progress project, created during another project development. I've found really difficult to find useful information on how to build a 2d strategy game, so my plan is to gather here all snippets, utilities and finding I'm able to found.

## Description and examples

### Flood-fill selection

Let's start with a simple 2d map, displaying nation borders, where every country has a different color. In this case how it's possible to select a country when user click on the map and display borders, so player can easily understand which country is selected ? 

*Show me the code*: [take a look here](https://github.com/Gornova/StrategyGameUtils/blob/master/desktop/src/it/randomtower/strategygameutils/desktop/SelectMapBorderFloodFill.java)

To solve this kind of problems I've found a simple solution:

1. get player click point as (x,y)
2. from (x,y) use [flood fill algorithm](https://en.wikipedia.org/wiki/Flood_fill) to fill a specific area, delimited by border color
3. draw a border around the resulting image, not used areas are transparent
4. display image with border to the player

Here a video that explain solution

[![Flood fill border selection example](http://img.youtube.com/vi/zeer0N1cj_Y/0.jpg)](http://www.youtube.com/watch?v=zeer0N1cj_Y "Flood fill border selection example")

*Notes*: with this solution map designer can change map in any way, just remember to keep same color for each border, without storying any geometries about borders. Next steps could be to display, using libgdx Stage a window and get some informations about a country
 


