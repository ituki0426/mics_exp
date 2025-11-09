
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class TAN {
    // Number of Categories

    int NC = 2;
    // Number of Variables
    int NV;
    // Leaning Data
    ArrayList<int[]> LD = new ArrayList<>();
    // Test Data
    ArrayList<int[]> TD = new ArrayList<>();
    //頻度表
    double[] ft_c;
    ArrayList<double[][]> ft_ic;
    ArrayList<ArrayList<double[][][]>> ft_ijc;//N_{X_i=k,X_j=m,X_0=c}の格納先．例えば，ft_ijc[1][0][0]=N_{X_i=1,X_j=0,X_0=0}, ft_ijc[0][1][0]=N_{X_i=0,X_j=1,X_0=0}となる(X_0は目的変数を表す)．

    public TAN() throws IOException {
        //データセット入力
        String dataset = "spam";
        System.out.println("Dataset: " + dataset);
        //学習データ読み込み
        readData("data/" + dataset + "/LD.csv", LD);
        //頻度表生成
        setFrequencyTable();
        //条件付き相互情報量
        double[][] cmi;
        cmi = getConditionalMutualInformation();
        //TANの構造学習
        int[] str_tan = getMaximumSpanningTree(cmi);
        //テストデータ読み込み
        readData("data/" + dataset + "/TD.csv", TD);
        //TANのパラメータ学習
        ArrayList<HashMap<Integer, Double>> parameters = getParameters(str_tan);
        //学習したTANで分類
        classification(parameters, str_tan);
    }

    // 変数自身とその親の値を鍵としてその条件付き確率パラメータを返すようなHashMapを，各変数ごとにArrayListで繋いだものを返す関数
    private ArrayList<HashMap<Integer, Double>> getParameters(int[] str_tan) {
        ArrayList<HashMap<Integer, Double>> parameters = new ArrayList<>();

        return parameters;
    }

    //パラメータを用いてテストデータの分類をし，その正答率を表示する．
    private void classification(ArrayList<HashMap<Integer, Double>> parameters, int[] str_tan) {
        double num_correct_prediction = 0;
        for (int d = 0; d < TD.size(); ++d) {

            //正答回数測定
            if (predictedClass == TD.get(d)[NV - 1]) {
                num_correct_prediction++;
            }
        }
        //正答率表示
        System.out.println("Classification accuracy: " + num_correct_prediction / (double) TD.size());
    }

    // データ読み込み
    private void readData(String DataBaseName, ArrayList<int[]> RD) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File(DataBaseName)));
        ArrayList<String[]> tRD = new ArrayList<String[]>();
        br.readLine();
        String line = br.readLine();
        NV = line.split(",").length;
        while (line != null) {
            String[] data = line.split(",").clone();
            tRD.add(data);
            line = br.readLine();
        }
        for (int i = 0; i < tRD.size(); ++i) {
            RD.add(new int[NV]);
            for (int j = 0; j < NV; ++j) {
                RD.get(i)[j] = Integer.parseInt(tRD.get(i)[j]);
            }
        }
        br.close();
    }

    //N_{X_0=c}, N_{X_i=k, X_0=c},N_{X_i=k,X_j=m,X_0=c}をそれぞれ ft_c, ft_ic, ft_ijc に格納する関数
    private void setFrequencyTable() {
        //初期化
        ft_c = new double[NC];
        ft_ic = new ArrayList<>();
        ft_ijc = new ArrayList<>();
        for (int i = 0; i < NV - 1; ++i) {
            ft_ic.add(new double[NC][NC]);
            ft_ijc.add(new ArrayList<>());
            //i < jについて，ft_ijc.get(i).get(j)とft_ijc.get(j).get(i)は同じ頻度データを保持するため，ft_ijc.get(i).get(j)だけに頻度データを格納すればよい．
            for (int empty = 0; empty <= i; ++empty) {
                ft_ijc.get(i).add(null);
            }
            for (int j = i + 1; j < NV - 1; ++j) {
                ft_ijc.get(i).add(new double[NC][NC][NC]);
            }
        }
        //ft_c, ft_ic, ft_ijcを計算
        for (int d = 0; d < LD.size(); ++d) {

        }
    }

    //ft_c, ft_ic, ft_ijcを用いて条件付き相互情報量を求める
    private double[][] getConditionalMutualInformation() {
        double[][] cmi = new double[NV - 1][NV - 1]; //例えば，cmi[2][5]=変数X_2,X_5間の条件付き相互情報量I(X_2,X_5|X_0)

        return cmi;
    }

    // 条件付き相互情報量を重みとして最大全域木を求める関数
    // 説明変数の構成する木構造のルートノードはデータの一番左側の列のノードとする．
    // 各変数の親変数(目的変数以外)を格納した配列を返す．
    private int[] getMaximumSpanningTree(double[][] cmi) {

    }

    public static void main(String[] args) throws IOException {
        new TAN();
    }
}
