package com.baulsupp.kolja.log.viewer;

import java.util.ArrayList;
import java.util.List;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.line.LineIndex;
import com.baulsupp.kolja.log.line.LineListener;
import com.baulsupp.kolja.log.util.IntRange;

class MockLineIndex implements LineIndex {
    private List<Line> lines = new ArrayList<Line>();

    public List<Line> get(IntRange intRange) {
        List<Line> result = new ArrayList<Line>();

        for (Line l: lines) {
            if (intRange.contains(l.getOffset())) {
                result.add(l);
            }
        }

        return result;
    }

    public int length() {
        return lines.isEmpty() ? 0 : (lines.get(lines.size() - 1).getOffset() + 1);
    }

    public void addLineListener(LineListener lineListener) {
    }

    public void addLine(Line line) {
        lines.add(line);
    }

    public void removeLineListener(LineListener listener) {
      // TODO Auto-generated method stub
      
    }
}
