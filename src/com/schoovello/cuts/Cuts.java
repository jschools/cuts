package com.schoovello.cuts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Cuts {

    public static void main(String[] args) {
        List<Integer> fullSizeBoards = new ArrayList<Integer>();
        for (String string : args) {
            fullSizeBoards.add(Integer.valueOf(string));
        }

        System.out.println("Input lengths: " + fullSizeBoards);

        List<Integer> neededLengths = new ArrayList<Integer>();
        Scanner scan = new Scanner(System.in);
        while (scan.hasNextLine()) {
			String trimmed = scan.nextLine().trim();
			if (trimmed.length() == 0) {
				break;
			}
			neededLengths.add(Integer.valueOf(trimmed));
        }

        BoardCutter boardCutter = new BoardCutter(fullSizeBoards, neededLengths);
        boardCutter.countBoards();

        System.out.print("\n\nDone.\n");
    }

    private static class BoardCutter {

        private List<Integer> fullSizeBoards;
        private List<Integer> neededLengths;
        private List<Integer> scraps;

        private BoardCutter(List<Integer> fullSizeBoards, List<Integer> neededLengths) {
            this.fullSizeBoards = fullSizeBoards;
            this.neededLengths = neededLengths;
            this.scraps = new ArrayList<Integer>();
        }

        public void countBoards() {
            Collections.sort(fullSizeBoards);
            Collections.sort(neededLengths, new Comparator<Integer>() {
                @Override
                public int compare(Integer lhs, Integer rhs) {
                    return -lhs.compareTo(rhs);
                }
            });

            for (Integer neededLength : neededLengths) {
                int needed = neededLength.intValue();

                System.out.println("Finding a board for length " + needed);

                boolean usedScrap = findScrapForLength(needed);
                if (usedScrap) {
                    System.out.println("used scrap");
                } else {
                    int newBoard = buySmallestPossibleNewBoard(needed);
                    System.out.println("BUY " + newBoard);

                    int remainder = newBoard - needed;
                    addScrap(remainder);
                }

                System.out.println("Scraps: " + scraps.toString());

            }
        }

        private boolean findScrapForLength(int length) {
            final int numScraps = scraps.size();
            for (int i = 0; i < numScraps; i++) {
                int scrapLength = scraps.get(i).intValue();
                if (scrapLength == length) {
                    scraps.set(i, Integer.valueOf(0));
                    sortScraps();
                    return true;
                } else if (scrapLength > length) {
                    scrapLength -= length;
                    scraps.set(i, Integer.valueOf(scrapLength));
                    sortScraps();
                    return true;
                }
            }
            return false;
        }

        private int buySmallestPossibleNewBoard(int neededLength) {
            for (Integer length : fullSizeBoards) {
                int intVal = length.intValue();
                if (intVal >= neededLength) {
                    return intVal;
                }
            }

            throw new IllegalStateException("no boards are long enough to give you a cut of length " + neededLength);
        }

        private void addScrap(int scrapLength) {
            System.out.println("SCRAP " + scrapLength);

            final int numScraps = scraps.size();

            boolean replacedZero = false;
            for (int i = 0; i < numScraps; i++) {
                int scrapSize = scraps.get(i).intValue();
                if (scrapSize == 0) {
                    scraps.set(i, Integer.valueOf(scrapLength));
                    replacedZero = true;
                    break;
                }
            }

            if (!replacedZero) {
                scraps.add(Integer.valueOf(scrapLength));
            }

            sortScraps();
        }

        private void sortScraps() {
            Collections.sort(scraps);
        }

    }
}
