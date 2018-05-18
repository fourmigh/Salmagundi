package org.caojun.svgmap

import android.graphics.Path
import java.util.ArrayList

object PathParser {

    /**
     * Copies elements from `original` into a new array, from indexes start (inclusive) to
     * end (exclusive). The original order of elements is preserved.
     * If `end` is greater than `original.length`, the result is padded
     * with the value `0.0f`.
     *
     * @param original the original array
     * @param start    the start index, inclusive
     * @param end      the end index, exclusive
     * @return the new array
     * @throws IllegalArgumentException       if `start > end`
     * @throws NullPointerException           if `original == null`
     */
    private fun copyOfRange(original: FloatArray, start: Int, end: Int): FloatArray {
        if (start > end) {
            throw IllegalArgumentException()
        }
        val originalLength = original.size
        if (start < 0 || start > originalLength) {
            throw ArrayIndexOutOfBoundsException()
        }
        val resultLength = end - start
        val copyLength = Math.min(resultLength, originalLength - start)
        val result = FloatArray(resultLength)
        System.arraycopy(original, start, result, 0, copyLength)
        return result
    }

    /**
     * @param pathData The string representing a path, the same as "d" string in svg file.
     * @return the generated Path object.
     */
    fun createPathFromPathData(pathData: String): Path? {
        val path = Path()
        val nodes = createNodesFromPathData(pathData)
        if (nodes != null) {
            try {
                PathDataNode.nodesToPath(nodes, path)
            } catch (e: RuntimeException) {
                throw RuntimeException("Error in parsing $pathData", e)
            }

            return path
        }
        return null
    }

    /**
     * @param pathData The string representing a path, the same as "d" string in svg file.
     * @return an array of the PathDataNode.
     */
    fun createNodesFromPathData(pathData: String?): Array<PathDataNode>? {
        if (pathData == null) {
            return null
        }
        var start = 0
        var end = 1

        val list = ArrayList<PathDataNode>()
        while (end < pathData.length) {
            end = nextStart(pathData, end)
            val s = pathData.substring(start, end).trim { it <= ' ' }
            if (s.length > 0) {
                val `val` = getFloats(s)
                addNode(list, s[0], `val`)
            }

            start = end
            end++
        }
        if (end - start == 1 && start < pathData.length) {
            addNode(list, pathData[start], FloatArray(0))
        }
        return list.toTypedArray()
    }

    private fun nextStart(s: String, end: Int): Int {
        var end = end
        var c: Char

        while (end < s.length) {
            c = s[end]
            // Note that 'e' or 'E' are not valid path commands, but could be
            // used for floating point numbers' scientific notation.
            // Therefore, when searching for next command, we should ignore 'e'
            // and 'E'.
            if (((c - 'A') * (c - 'Z') <= 0 || (c - 'a') * (c - 'z') <= 0)
                    && c != 'e' && c != 'E') {
                return end
            }
            end++
        }
        return end
    }

    private fun addNode(list: ArrayList<PathDataNode>, cmd: Char, `val`: FloatArray) {
        list.add(PathDataNode(cmd, `val`))
    }

    private class ExtractFloatResult {
        // We need to return the position of the next separator and whether the
        // next float starts with a '-' or a '.'.
        internal var mEndPosition: Int = 0
        internal var mEndWithNegOrDot: Boolean = false
    }

    /**
     * Parse the floats in the string.
     * This is an optimized version of parseFloat(s.split(",|\\s"));
     *
     * @param s the string containing a command and list of floats
     * @return array of floats
     */
    private fun getFloats(s: String): FloatArray {
        if ((s[0] == 'z') or (s[0] == 'Z')) {
            return FloatArray(0)
        }
        try {
            val results = FloatArray(s.length)
            var count = 0
            var startPosition = 1
            var endPosition = 0

            val result = ExtractFloatResult()
            val totalLength = s.length

            // The startPosition should always be the first character of the
            // current number, and endPosition is the character after the current
            // number.
            while (startPosition < totalLength) {
                extract(s, startPosition, result)
                endPosition = result.mEndPosition

                if (startPosition < endPosition) {
                    results[count++] = java.lang.Float.parseFloat(
                            s.substring(startPosition, endPosition))
                }

                if (result.mEndWithNegOrDot) {
                    // Keep the '-' or '.' sign with next number.
                    startPosition = endPosition
                } else {
                    startPosition = endPosition + 1
                }
            }
            return copyOfRange(results, 0, count)
        } catch (e: NumberFormatException) {
            throw RuntimeException("error in parsing \"$s\"", e)
        }

    }

    /**
     * Calculate the position of the next comma or space or negative sign
     *
     * @param s      the string to search
     * @param start  the position to start searching
     * @param result the result of the extraction, including the position of the
     * the starting position of next number, whether it is ending with a '-'.
     */
    private fun extract(s: String, start: Int, result: ExtractFloatResult) {
        // Now looking for ' ', ',', '.' or '-' from the start.
        var currentIndex = start
        var foundSeparator = false
        result.mEndWithNegOrDot = false
        var secondDot = false
        var isExponential = false
        while (currentIndex < s.length) {
            val isPrevExponential = isExponential
            isExponential = false
            val currentChar = s[currentIndex]
            when (currentChar) {
                ' ', ',' -> foundSeparator = true
                '-' ->
                    // The negative sign following a 'e' or 'E' is not a separator.
                    if (currentIndex != start && !isPrevExponential) {
                        foundSeparator = true
                        result.mEndWithNegOrDot = true
                    }
                '.' -> if (!secondDot) {
                    secondDot = true
                } else {
                    // This is the second dot, and it is considered as a separator.
                    foundSeparator = true
                    result.mEndWithNegOrDot = true
                }
                'e', 'E' -> isExponential = true
            }
            if (foundSeparator) {
                break
            }
            currentIndex++
        }
        // When there is nothing found, then we put the end position to the end
        // of the string.
        result.mEndPosition = currentIndex
    }

    /**
     * Each PathDataNode represents one command in the "d" attribute of the svg
     * file.
     * An array of PathDataNode can represent the whole "d" attribute.
     */
    class PathDataNode {
        /*package*/
        internal var type: Char = ' '
        internal var params: FloatArray

        constructor(type: Char, params: FloatArray) {
            this.type = type
            this.params = params
        }

        companion object {

            /**
             * Convert an array of PathDataNode to Path.
             *
             * @param node The source array of PathDataNode.
             * @param path The target Path object.
             */
            fun nodesToPath(node: Array<PathDataNode>, path: Path) {
                val current = FloatArray(6)
                var previousCommand = 'm'
                for (i in node.indices) {
                    addCommand(path, current, previousCommand, node[i].type, node[i].params)
                    previousCommand = node[i].type
                }
            }

            private fun addCommand(path: Path, current: FloatArray,
                                   previousCmd: Char, cmd: Char, `val`: FloatArray) {
                var previousCmd = previousCmd

                var incr = 2
                var currentX = current[0]
                var currentY = current[1]
                var ctrlPointX = current[2]
                var ctrlPointY = current[3]
                var currentSegmentStartX = current[4]
                var currentSegmentStartY = current[5]
                var reflectiveCtrlPointX: Float
                var reflectiveCtrlPointY: Float

                when (cmd) {
                    'z', 'Z' -> {
                        path.close()
                        // Path is closed here, but we need to move the pen to the
                        // closed position. So we cache the segment's starting position,
                        // and restore it here.
                        currentX = currentSegmentStartX
                        currentY = currentSegmentStartY
                        ctrlPointX = currentSegmentStartX
                        ctrlPointY = currentSegmentStartY
                        path.moveTo(currentX, currentY)
                    }
                    'm', 'M', 'l', 'L', 't', 'T' -> incr = 2
                    'h', 'H', 'v', 'V' -> incr = 1
                    'c', 'C' -> incr = 6
                    's', 'S', 'q', 'Q' -> incr = 4
                    'a', 'A' -> incr = 7
                }

                var k = 0
                while (k < `val`.size) {
                    when (cmd) {
                        'm' // moveto - Start a new sub-path (relative)
                        -> {
                            currentX += `val`[k + 0]
                            currentY += `val`[k + 1]
                            if (k > 0) {
                                // According to the spec, if a moveto is followed by multiple
                                // pairs of coordinates, the subsequent pairs are treated as
                                // implicit lineto commands.
                                path.rLineTo(`val`[k + 0], `val`[k + 1])
                            } else {
                                path.rMoveTo(`val`[k + 0], `val`[k + 1])
                                currentSegmentStartX = currentX
                                currentSegmentStartY = currentY
                            }
                        }
                        'M' // moveto - Start a new sub-path
                        -> {
                            currentX = `val`[k + 0]
                            currentY = `val`[k + 1]
                            if (k > 0) {
                                // According to the spec, if a moveto is followed by multiple
                                // pairs of coordinates, the subsequent pairs are treated as
                                // implicit lineto commands.
                                path.lineTo(`val`[k + 0], `val`[k + 1])
                            } else {
                                path.moveTo(`val`[k + 0], `val`[k + 1])
                                currentSegmentStartX = currentX
                                currentSegmentStartY = currentY
                            }
                        }
                        'l' // lineto - Draw a line from the current point (relative)
                        -> {
                            path.rLineTo(`val`[k + 0], `val`[k + 1])
                            currentX += `val`[k + 0]
                            currentY += `val`[k + 1]
                        }
                        'L' // lineto - Draw a line from the current point
                        -> {
                            path.lineTo(`val`[k + 0], `val`[k + 1])
                            currentX = `val`[k + 0]
                            currentY = `val`[k + 1]
                        }
                        'h' // horizontal lineto - Draws a horizontal line (relative)
                        -> {
                            path.rLineTo(`val`[k + 0], 0f)
                            currentX += `val`[k + 0]
                        }
                        'H' // horizontal lineto - Draws a horizontal line
                        -> {
                            path.lineTo(`val`[k + 0], currentY)
                            currentX = `val`[k + 0]
                        }
                        'v' // vertical lineto - Draws a vertical line from the current point (r)
                        -> {
                            path.rLineTo(0f, `val`[k + 0])
                            currentY += `val`[k + 0]
                        }
                        'V' // vertical lineto - Draws a vertical line from the current point
                        -> {
                            path.lineTo(currentX, `val`[k + 0])
                            currentY = `val`[k + 0]
                        }
                        'c' // curveto - Draws a cubic Bézier curve (relative)
                        -> {
                            path.rCubicTo(`val`[k + 0], `val`[k + 1], `val`[k + 2], `val`[k + 3],
                                    `val`[k + 4], `val`[k + 5])

                            ctrlPointX = currentX + `val`[k + 2]
                            ctrlPointY = currentY + `val`[k + 3]
                            currentX += `val`[k + 4]
                            currentY += `val`[k + 5]
                        }
                        'C' // curveto - Draws a cubic Bézier curve
                        -> {
                            path.cubicTo(`val`[k + 0], `val`[k + 1], `val`[k + 2], `val`[k + 3],
                                    `val`[k + 4], `val`[k + 5])
                            currentX = `val`[k + 4]
                            currentY = `val`[k + 5]
                            ctrlPointX = `val`[k + 2]
                            ctrlPointY = `val`[k + 3]
                        }
                        's' // smooth curveto - Draws a cubic Bézier curve (reflective cp)
                        -> {
                            reflectiveCtrlPointX = 0f
                            reflectiveCtrlPointY = 0f
                            if (previousCmd == 'c' || previousCmd == 's'
                                    || previousCmd == 'C' || previousCmd == 'S') {
                                reflectiveCtrlPointX = currentX - ctrlPointX
                                reflectiveCtrlPointY = currentY - ctrlPointY
                            }
                            path.rCubicTo(reflectiveCtrlPointX, reflectiveCtrlPointY,
                                    `val`[k + 0], `val`[k + 1],
                                    `val`[k + 2], `val`[k + 3])

                            ctrlPointX = currentX + `val`[k + 0]
                            ctrlPointY = currentY + `val`[k + 1]
                            currentX += `val`[k + 2]
                            currentY += `val`[k + 3]
                        }
                        'S' // shorthand/smooth curveto Draws a cubic Bézier curve(reflective cp)
                        -> {
                            reflectiveCtrlPointX = currentX
                            reflectiveCtrlPointY = currentY
                            if (previousCmd == 'c' || previousCmd == 's'
                                    || previousCmd == 'C' || previousCmd == 'S') {
                                reflectiveCtrlPointX = 2 * currentX - ctrlPointX
                                reflectiveCtrlPointY = 2 * currentY - ctrlPointY
                            }
                            path.cubicTo(reflectiveCtrlPointX, reflectiveCtrlPointY,
                                    `val`[k + 0], `val`[k + 1], `val`[k + 2], `val`[k + 3])
                            ctrlPointX = `val`[k + 0]
                            ctrlPointY = `val`[k + 1]
                            currentX = `val`[k + 2]
                            currentY = `val`[k + 3]
                        }
                        'q' // Draws a quadratic Bézier (relative)
                        -> {
                            path.rQuadTo(`val`[k + 0], `val`[k + 1], `val`[k + 2], `val`[k + 3])
                            ctrlPointX = currentX + `val`[k + 0]
                            ctrlPointY = currentY + `val`[k + 1]
                            currentX += `val`[k + 2]
                            currentY += `val`[k + 3]
                        }
                        'Q' // Draws a quadratic Bézier
                        -> {
                            path.quadTo(`val`[k + 0], `val`[k + 1], `val`[k + 2], `val`[k + 3])
                            ctrlPointX = `val`[k + 0]
                            ctrlPointY = `val`[k + 1]
                            currentX = `val`[k + 2]
                            currentY = `val`[k + 3]
                        }
                        't' // Draws a quadratic Bézier curve(reflective control point)(relative)
                        -> {
                            reflectiveCtrlPointX = 0f
                            reflectiveCtrlPointY = 0f
                            if (previousCmd == 'q' || previousCmd == 't'
                                    || previousCmd == 'Q' || previousCmd == 'T') {
                                reflectiveCtrlPointX = currentX - ctrlPointX
                                reflectiveCtrlPointY = currentY - ctrlPointY
                            }
                            path.rQuadTo(reflectiveCtrlPointX, reflectiveCtrlPointY,
                                    `val`[k + 0], `val`[k + 1])
                            ctrlPointX = currentX + reflectiveCtrlPointX
                            ctrlPointY = currentY + reflectiveCtrlPointY
                            currentX += `val`[k + 0]
                            currentY += `val`[k + 1]
                        }
                        'T' // Draws a quadratic Bézier curve (reflective control point)
                        -> {
                            reflectiveCtrlPointX = currentX
                            reflectiveCtrlPointY = currentY
                            if (previousCmd == 'q' || previousCmd == 't'
                                    || previousCmd == 'Q' || previousCmd == 'T') {
                                reflectiveCtrlPointX = 2 * currentX - ctrlPointX
                                reflectiveCtrlPointY = 2 * currentY - ctrlPointY
                            }
                            path.quadTo(reflectiveCtrlPointX, reflectiveCtrlPointY,
                                    `val`[k + 0], `val`[k + 1])
                            ctrlPointX = reflectiveCtrlPointX
                            ctrlPointY = reflectiveCtrlPointY
                            currentX = `val`[k + 0]
                            currentY = `val`[k + 1]
                        }
                        'a' // Draws an elliptical arc
                        -> {
                            // (rx ry x-axis-rotation large-arc-flag sweep-flag x y)
                            drawArc(path,
                                    currentX,
                                    currentY,
                                    `val`[k + 5] + currentX,
                                    `val`[k + 6] + currentY,
                                    `val`[k + 0],
                                    `val`[k + 1],
                                    `val`[k + 2],
                                    `val`[k + 3] != 0f,
                                    `val`[k + 4] != 0f)
                            currentX += `val`[k + 5]
                            currentY += `val`[k + 6]
                            ctrlPointX = currentX
                            ctrlPointY = currentY
                        }
                        'A' // Draws an elliptical arc
                        -> {
                            drawArc(path,
                                    currentX,
                                    currentY,
                                    `val`[k + 5],
                                    `val`[k + 6],
                                    `val`[k + 0],
                                    `val`[k + 1],
                                    `val`[k + 2],
                                    `val`[k + 3] != 0f,
                                    `val`[k + 4] != 0f)
                            currentX = `val`[k + 5]
                            currentY = `val`[k + 6]
                            ctrlPointX = currentX
                            ctrlPointY = currentY
                        }
                    }
                    previousCmd = cmd
                    k += incr
                }
                current[0] = currentX
                current[1] = currentY
                current[2] = ctrlPointX
                current[3] = ctrlPointY
                current[4] = currentSegmentStartX
                current[5] = currentSegmentStartY
            }

            private fun drawArc(p: Path,
                                x0: Float,
                                y0: Float,
                                x1: Float,
                                y1: Float,
                                a: Float,
                                b: Float,
                                theta: Float,
                                isMoreThanHalf: Boolean,
                                isPositiveArc: Boolean) {

                /* Convert rotation angle from degrees to radians */
                val thetaD = Math.toRadians(theta.toDouble())
                /* Pre-compute rotation matrix entries */
                val cosTheta = Math.cos(thetaD)
                val sinTheta = Math.sin(thetaD)
                /* Transform (x0, y0) and (x1, y1) into unit space */
                /* using (inverse) rotation, followed by (inverse) scale */
                val x0p = (x0 * cosTheta + y0 * sinTheta) / a
                val y0p = (-x0 * sinTheta + y0 * cosTheta) / b
                val x1p = (x1 * cosTheta + y1 * sinTheta) / a
                val y1p = (-x1 * sinTheta + y1 * cosTheta) / b

                /* Compute differences and averages */
                val dx = x0p - x1p
                val dy = y0p - y1p
                val xm = (x0p + x1p) / 2
                val ym = (y0p + y1p) / 2
                /* Solve for intersecting unit circles */
                val dsq = dx * dx + dy * dy
                if (dsq == 0.0) {
                    return  /* Points are coincident */
                }
                val disc = 1.0 / dsq - 1.0 / 4.0
                if (disc < 0.0) {
                    val adjust = (Math.sqrt(dsq) / 1.99999).toFloat()
                    drawArc(p, x0, y0, x1, y1, a * adjust,
                            b * adjust, theta, isMoreThanHalf, isPositiveArc)
                    return  /* Points are too far apart */
                }
                val s = Math.sqrt(disc)
                val sdx = s * dx
                val sdy = s * dy
                var cx: Double
                var cy: Double
                if (isMoreThanHalf == isPositiveArc) {
                    cx = xm - sdy
                    cy = ym + sdx
                } else {
                    cx = xm + sdy
                    cy = ym - sdx
                }

                val eta0 = Math.atan2(y0p - cy, x0p - cx)

                val eta1 = Math.atan2(y1p - cy, x1p - cx)

                var sweep = eta1 - eta0
                if (isPositiveArc != sweep >= 0) {
                    if (sweep > 0) {
                        sweep -= 2 * Math.PI
                    } else {
                        sweep += 2 * Math.PI
                    }
                }

                cx *= a.toDouble()
                cy *= b.toDouble()
                cx = cx * cosTheta - cy * sinTheta
                cy = cx * sinTheta + cy * cosTheta

                arcToBezier(p, cx, cy, a.toDouble(), b.toDouble(), x0.toDouble(), y0.toDouble(), thetaD, eta0, sweep)
            }

            /**
             * Converts an arc to cubic Bezier segments and records them in p.
             *
             * @param p     The target for the cubic Bezier segments
             * @param cx    The x coordinate center of the ellipse
             * @param cy    The y coordinate center of the ellipse
             * @param a     The radius of the ellipse in the horizontal direction
             * @param b     The radius of the ellipse in the vertical direction
             * @param e1x   E(eta1) x coordinate of the starting point of the arc
             * @param e1y   E(eta2) y coordinate of the starting point of the arc
             * @param theta The angle that the ellipse bounding rectangle makes with horizontal plane
             * @param start The start angle of the arc on the ellipse
             * @param sweep The angle (positive or negative) of the sweep of the arc on the ellipse
             */
            private fun arcToBezier(p: Path,
                                    cx: Double,
                                    cy: Double,
                                    a: Double,
                                    b: Double,
                                    e1x: Double,
                                    e1y: Double,
                                    theta: Double,
                                    start: Double,
                                    sweep: Double) {
                var e1x = e1x
                var e1y = e1y
                // Taken from equations at: http://spaceroots.org/documents/ellipse/node8.html
                // and http://www.spaceroots.org/documents/ellipse/node22.html

                // Maximum of 45 degrees per cubic Bezier segment
                val numSegments = Math.ceil(Math.abs(sweep * 4 / Math.PI)).toInt()

                var eta1 = start
                val cosTheta = Math.cos(theta)
                val sinTheta = Math.sin(theta)
                val cosEta1 = Math.cos(eta1)
                val sinEta1 = Math.sin(eta1)
                var ep1x = -a * cosTheta * sinEta1 - b * sinTheta * cosEta1
                var ep1y = -a * sinTheta * sinEta1 + b * cosTheta * cosEta1

                val anglePerSegment = sweep / numSegments
                for (i in 0 until numSegments) {
                    val eta2 = eta1 + anglePerSegment
                    val sinEta2 = Math.sin(eta2)
                    val cosEta2 = Math.cos(eta2)
                    val e2x = cx + a * cosTheta * cosEta2 - b * sinTheta * sinEta2
                    val e2y = cy + a * sinTheta * cosEta2 + b * cosTheta * sinEta2
                    val ep2x = -a * cosTheta * sinEta2 - b * sinTheta * cosEta2
                    val ep2y = -a * sinTheta * sinEta2 + b * cosTheta * cosEta2
                    val tanDiff2 = Math.tan((eta2 - eta1) / 2)
                    val alpha = Math.sin(eta2 - eta1) * (Math.sqrt(4 + 3.0 * tanDiff2 * tanDiff2) - 1) / 3
                    val q1x = e1x + alpha * ep1x
                    val q1y = e1y + alpha * ep1y
                    val q2x = e2x - alpha * ep2x
                    val q2y = e2y - alpha * ep2y

                    p.cubicTo(q1x.toFloat(),
                            q1y.toFloat(),
                            q2x.toFloat(),
                            q2y.toFloat(),
                            e2x.toFloat(),
                            e2y.toFloat())
                    eta1 = eta2
                    e1x = e2x
                    e1y = e2y
                    ep1x = ep2x
                    ep1y = ep2y
                }
            }
        }
    }
}