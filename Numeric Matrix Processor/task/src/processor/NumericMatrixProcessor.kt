package processor

import processor.NumericMatrixProcessor.matrix.cNum
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class NumericMatrixProcessor {

    private var aNum = mutableListOf<String>()
    private var bNum = mutableListOf<String>()

    fun mainMenu() {
        do {
            print("1. Add matrices\n" +
                    "2. Multiply matrix by a constant\n" +
                    "3. Multiply matrices\n"+
                    "4. Transpose matrix\n"+
                    "5. Calculate a determinant\n"+
                    "6. Inverse matrix\n"+
                    "0. Exit\n" +
                    "Your choice: ")
            when (readLine()!!) {
                "1" -> addMatrices()
                "2" -> multiplicationByNumber()
                "3" -> multiplyMatrices()
                "4" -> transposeMatrixMenu()
                "5" -> calculateDeterminant()
                "6" -> inverseMatrix()
                "0" -> System.exit(-1)
                else -> continue
            }
        } while (true)
    }
    private fun inverseMatrix() {
        val b = matrix
        val a = b.inputMatrixTrans()
        val n = b.cNum[0].toInt()
        val (sigmas, signs) = calculation(a.size)
        var sum = 0.0
        for ((i, sigma) in sigmas.withIndex()) {
            var prod = 1.0
            for ((j, s) in sigma.withIndex()) prod *= a[j][s]
            sum += signs[i] * prod
        }
        if (sum > 0 ) {
            var temp: Double
            val e = Array(n) { FloatArray(n) }
            for (i in 0 until n) for (j in 0 until n) {
                e[i][j] = 0f
                if (i == j) e[i][j] = 1f
            }
            for (k in 0 until n) {
                temp = a[k][k]
                for (j in 0 until n) {
                    a[k][j] /= temp
                    e[k][j] /= temp.toFloat()
                }
                for (i in k + 1 until n) {
                    temp = a[i][k]
                    for (j in 0 until n) {
                        a[i][j] -= a[k][j] * temp
                        e[i][j] -= (e[k][j] * temp).toFloat()
                    }
                }
            }
            for (k in n - 1 downTo 1) {
                for (i in k - 1 downTo 0) {
                    temp = a[i][k]
                    for (j in 0 until n) {
                        a[i][j] -= a[k][j] * temp
                        e[i][j] -= (e[k][j] * temp).toFloat()
                    }
                }
            }
            println("The result is:")
            for (i in 0 until n) {
                for (j in 0 until n) {
                    a[i][j] = e[i][j].toDouble()
                    print("${roundOffDecimal(a[i][j])} ")
                }
                println()
            }
            mainMenu()
        } else {
            println("This matrix doesn't have an inverse.")
            println()
            mainMenu()
        }
    }
    private fun roundOffDecimal(number: Double): Double {
        val df = DecimalFormat("#.##", DecimalFormatSymbols(Locale.ENGLISH))
        df.roundingMode = RoundingMode.FLOOR
        try {
            df.format(number).toDouble()
        }
        catch (e: Exception) {
            println("This matrix doesn't have an inverse.")
            mainMenu()
        }
        return df.format(number).toDouble()
    }

     private fun calculateDeterminant() {
        val b = matrix
        val m = b.inputMatrixTrans()
        val (sigmas, signs) = calculation(m.size)
        var sum = 0.0
        for ((i, sigma) in sigmas.withIndex()) {
            var prod = 1.0
            for ((j, s) in sigma.withIndex()) prod *= m[j][s]
            sum += signs[i] * prod
        }
        println("The result is:")
        println(sum)
        mainMenu()
    }

    fun calculation(n: Int): Pair<List<IntArray>, List<Int>> {
        val p = IntArray(n) { it }
        val q = IntArray(n) { it }
        val d = IntArray(n) { -1 }
        var sign = 1
        val perms = mutableListOf<IntArray>()
        val signs = mutableListOf<Int>()

        fun permute(k: Int) {
            if (k >= n) {
                perms.add(p.copyOf())
                signs.add(sign)
                sign *= -1
                return
            }
            permute(k + 1)
            for (i in 0 until k) {
                val z = p[q[k] + d[k]]
                p[q[k]] = z
                p[q[k] + d[k]] = k
                q[z] = q[k]
                q[k] += d[k]
                permute(k + 1)
            }
            d[k] *= -1
        }

        permute(0)
        return perms to signs
    }

    private fun addMatrices() {
        println("Enter size of first matrix:")
        aNum = readLine()!!.split(" ").toMutableList()
        println("Enter first matrix:")
        val a = inputMatrix(aNum[0].toInt(),aNum[1].toInt())
        println("Enter size of second matrix:")
        bNum = readLine()!!.split(" ").toMutableList()
        println("Enter second matrix:")
        val b = inputMatrix(bNum[0].toInt(),bNum[1].toInt())
        if (aNum[0] != bNum[0]) println("The operation cannot be performed.")
        else{
            println("The result is:")
            for (i in 0 until aNum[0].toInt()) {

                for (j in 0 until aNum[1].toInt()) {
                    print("${a[i][j] + b[i][j]} ")
                }
                println()
            }
        }
    }
    private fun multiplyMatrices() {
        println("Enter size of first matrix:")
        aNum = readLine()!!.split(" ").toMutableList()
        println("Enter first matrix:")
        val a = inputMatrix(aNum[0].toInt(),aNum[1].toInt())
        println("Enter size of second matrix:")
        bNum = readLine()!!.split(" ").toMutableList()
        println("Enter second matrix:")
        val b = inputMatrix(bNum[0].toInt(),bNum[1].toInt())
        var arrayRes = Array(aNum[0].toInt()) { DoubleArray(bNum[1].toInt()) }
        if (aNum[1] != bNum[0]) println("The operation cannot be performed.")
        else {
            println("The result is:")
            for ( i in 0 until aNum[0].toInt()) {
                for ( j in 0 until bNum[1].toInt()) {
                    for ( k in 0 until aNum[1].toInt()) {
                        arrayRes[i][j] += a[i][k] * b[k][j]
                    }
                }
            }
            for (i in 0 until aNum[0].toInt()) {
                for (j in 0 until bNum[1].toInt()) {
                    print("${arrayRes[i][j]} ")
                }
                println()
            }
        }
    }
    private fun multiplicationByNumber() {
        println("Enter size of matrix:")
        val cNum = readLine()!!.split(" ").toMutableList()
        println("Enter matrix:")
        val c = inputMatrix(cNum[0].toInt(),cNum[1].toInt())
        println("Enter constant: ")
        val ind = readLine()!!.toDouble()
        println("The result is:")
        for (i in 0 until cNum[0].toInt()) {
            for (j in 0 until cNum[1].toInt()) {
                print("${c[i][j] * ind} ")
            }
            println()
        }
    }
    private fun inputMatrix(rows: Int, columns: Int): Array<DoubleArray> {
        val arrayNew = Array(rows) { DoubleArray(columns) }
        for (i in 0 until rows) {
            val s = readLine()!!.split(" ").toMutableList()
            for (j in 0 until columns) {
                arrayNew[i][j] = s[j].toDouble()
            }
        }
        return arrayNew
    }


    private fun transposeMatrixMenu() {
            print("\n1. Main diagonal\n" +
                    "2. Side diagonal\n" +
                    "3. Vertical line\n" +
                    "4. Horizontal line\n" +
                    "Your choice: ")
            when (readLine()!!) {
                "1" -> mainDiagonal()
                "2" -> sideDiagonal()
                "3" -> verticalDiagonal()
                "4" -> horizontalDiagonal()
                else -> mainMenu()
        }
    }
    /* Объект создавался для покрытия тем в проэкте,
        а так же избежать дублирования кода
    */
        object matrix{
            var cNum = mutableListOf<String>()
            fun inputMatrixTrans(): Array<DoubleArray> {
                println("Enter size of matrix:")
                cNum = readLine()!!.split(" ") as MutableList<String>
                val arrayNew = Array(cNum[0].toInt()) { DoubleArray(cNum[1].toInt()) }
                println("Enter matrix:")
                for (i in 0 until cNum[0].toInt()) {
                    val s = readLine()!!.split(" ").toMutableList()
                    for (j in 0 until cNum[1].toInt()) {
                        arrayNew[i][j] = s[j].toDouble()
                    }
                }
                return arrayNew
            }
        }
        private fun horizontalDiagonal() {
            val b = matrix
            val mat = b.inputMatrixTrans()
            val n = cNum[0].toInt() / 2 - 1
            val n2 = cNum[0].toInt() - 1
            for (j in 0 until  cNum[1].toInt() ) {
                for (i in 0 .. n) {
                    if (n2 != i) {
                        val tmp = mat[i][j]
                        mat[i][j] = mat[n2 - i][j]
                        mat[n2 - i][j] = tmp
                    } else break
                }
            }
            for (i in 0 until cNum[0].toInt()) {
                for (j in 0 until cNum[1].toInt()) {
                    print("${mat[i][j]} ")
                }
                println()
            }
            mainMenu()
        }
        private fun verticalDiagonal() {
            val b = matrix
            val mat = b.inputMatrixTrans()
            val n = cNum[1].toInt() / 2 - 1
            val n2 = cNum[1].toInt() - 1
                for (i in 0 until  cNum[0].toInt() ) {
                    for (j in 0 .. n) {
                            val tmp = mat[i][j]
                            mat[i][j] = mat[i][n2 - j]
                            mat[i][n2 - j] = tmp
                    }
                }
                for (i in 0 until cNum[0].toInt()) {
                    for (j in 0 until cNum[1].toInt()) {
                        print("${mat[i][j]} ")
                    }
                    println()
                }
                mainMenu()
        }
        private fun sideDiagonal() {
            val b = matrix
            val mat = b.inputMatrixTrans()
            val n = cNum[0].toInt()
            if (cNum[0] == cNum[1]) {
                for (i in 0 until n - 1) {
                    for (j in 0 until n - 1 - i) {
                        val tmp = mat[i][j]
                        mat[i][j] = mat[n - 1 - j][n - 1 - i]
                        mat[n - 1 - j][n - 1 - i] = tmp
                    }
                }
                for (i in 0 until cNum[0].toInt()) {
                    for (j in 0 until cNum[1].toInt()) {
                        print("${mat[i][j]} ")
                    }
                    println()
                }
                println()
                mainMenu()
            } else {
                println("The operation cannot be performed.")
                transposeMatrixMenu()
            }

        }
        private fun mainDiagonal() {
            val a = matrix
            val mat = a.inputMatrixTrans()
            for (j in 0 until cNum[1].toInt()) {
                for (i in 0 until cNum[0].toInt()) {

                    print("${mat[i][j] } ")
                }
                println()
            }
            mainMenu()
        }
    }
