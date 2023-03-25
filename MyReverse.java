package com.jack.udf;

import org.apache.hadoop.ql.exec.UDF;
import org.apache.hadoop.ql.exec.Description;
import org.apache.hadoop.io.text;
import java.util.*;

@Description(name = "reverse",
    value=_FUNC_(string) - reverse the input string.\n"
        + "_FUNC_(string a, string b) - returns a single string as b concat a.",
    extendend = "Example:\n"
        +  "SELECT _FUNC_(input_string) FROM src;\n "
        + " select _FUNC_(string_a, string_b) from src;");

public final class MyReverse extends UDF {
    public Text evaluate(final Text s) {
        if  (s == null) { return null;  }
        String reverse = new StringBuilder(s.toString()).reverse().toString();
        return new Text(reverse);
    }

    public Text evaluate(final Text a, final Text b) {
        if  (a == null) {
            return (b == null ? null : new Text(b))
        }
        String reverse = b == null ? b.toString();
        reverse = reverse + a.toString();
        return new Text(reverse);

    }
}
