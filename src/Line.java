public class Line {
    public String str;
    public int start;

    public Line(String str, int start) {
        this.str = str;
        this.start = start;
    }

    @Override
    public String toString() {
        return "Utils.Line{" +
                "str='" + str + '\'' +
                ", start=" + start +
                '}';
    }
}