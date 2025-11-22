package org.raflab.studsluzba.utils;

public class ParseUtils {
	
	/*
	 * dobija indeks oblika rn1923 i vraca niz stringova [RN,19,23]
	 */
    public static String[] parseIndeks(String indeksShort) {
        if (indeksShort == null || indeksShort.isBlank()) return null;

        // Uklanjamo razmake
        indeksShort = indeksShort.trim().toUpperCase();

        // Regex koji hvata:
        // 1) STUDIJSKI PROGRAM: 1+ slova, opcioni brojevi  (npr. SP1, RI, RN2)
        // 2) GODINU: 2 cifre
        // 3) BROJ: 1-3 cifre
        String regex = "^([A-Z]+\\d*)[\\s\\-\\/_]*?(\\d{2})[\\s\\-\\/_]*?(\\d{1,3})$";

        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(indeksShort);

        if (!matcher.matches()) return null;

        String program = matcher.group(1);   // SP1, RI...
        String year = matcher.group(2);      // 21, 23...
        String number = matcher.group(3);    // 1, 05, 010...

        return new String[]{ program, year, number };
    }


    public static String[] parseEmail(String studEmail) {
        if (!studEmail.endsWith("@raf.rs")) return null;

        String emailStr = studEmail.substring(0, studEmail.indexOf('@')); // "astevanovic8424"

        // Prvo slovo imena
        char prvoSlovo = emailStr.charAt(0);

        // Broj indeksa i godina (poslednje 4 cifre)
        int len = emailStr.length();
        String brojGodina = emailStr.substring(len - 4); // npr. "8424"
        String broj = brojGodina.substring(0, brojGodina.length() - 2); // "84"
        String godina = brojGodina.substring(brojGodina.length() - 2);  // "24"

        // Prezime je između prvog slova i broja/godine
        String prezime = emailStr.substring(1, len - 4);

        return new String[] { (prvoSlovo + prezime).toLowerCase(), godina, broj };
    }



}
