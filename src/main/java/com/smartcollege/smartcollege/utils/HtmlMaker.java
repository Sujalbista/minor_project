package com.smartcollege.smartcollege.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HtmlMaker {
    public static String createMarksheetHtml(String term, String sem, String student, String parent, ResultSet result){
        String htmlcontent = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "  <head> </head>\n" +
                "  <body\n" +
                "    style=\"\n" +
                "      overflow: hidden;\n" +
                "      margin: 0px;\n" +
                "      height: 100vh;\n" +
                "      width: 100%;\n" +
                "      background: rgb(0, 77, 84);\n" +
                "      background: linear-gradient(\n" +
                "        320deg,\n" +
                "        rgba(0, 77, 84, 1) 0%,\n" +
                "        rgba(6, 18, 78, 1) 100%\n" +
                "      );\n" +
                "      color: white;\n" +
                "      font-family: sans-serif;\n" +
                "      display: flex;\n" +
                "      justify-content: center;\n" +
                "      align-items: center;\n" +
                "    \"\n" +
                "  >\n" +
                "    <div\n" +
                "      class=\"main\"\n" +
                "      style=\"\n" +
                "        position: relative;\n" +
                "        overflow-x: hidden;\n" +
                "        overflow-y: scroll;\n" +
                "        margin: 0px;\n" +
                "        height: 100vh;\n" +
                "        width: 100%;\n" +
                "        background: rgb(0, 77, 84);\n" +
                "        background: linear-gradient(\n" +
                "          320deg,\n" +
                "          rgba(0, 77, 84, 1) 0%,\n" +
                "          rgba(6, 18, 78, 1) 100%\n" +
                "        );\n" +
                "        color: white;\n" +
                "        font-family: sans-serif;\n" +
                "      \"\n" +
                "    >\n" +
                "      <div\n" +
                "        class=\"main2\"\n" +
                "        style=\"\n" +
                "          margin: 0 auto;\n" +
                "          display: block;\n" +
                "          margin-top: 100px;\n" +
                "          max-width: 500px;\n" +
                "          background-color: rgb(20, 20, 20);\n" +
                "          padding: 20px;\n" +
                "          box-shadow: rgba(0, 0, 0, 0.66) 0px 0px 100px;\n" +
                "          border-radius: 30px;\n" +
                "        \"\n" +
                "      >\n" +
                "        <div\n" +
                "          class=\"logocontainner\"\n" +
                "          style=\"\n" +
                "            width: 100%;\n" +
                "            height: auto;\n" +
                "            display: flex;\n" +
                "            justify-content: center;\n" +
                "            align-items: center;\n" +
                "          \"\n" +
                "        >\n" +
                "          <img\n" +
                "            class=\"logo\"\n" +
                "            src=\"https://raw.githubusercontent.com/Subu19/Campusflow/master/src/main/resources/images/logowithtext.png\"\n" +
                "            style=\"width: 80%; height: auto\"\n" +
                "          />\n" +
                "        </div>\n" +
                "        <h1 class=\"heading\">"+term+" marksheet</h1>\n" +
                "        <div class=\"text\">\n" +
                "          Dear "+parent+"<br />\n" +
                "          I hope this letter finds you well. I am writing to provide you with an\n" +
                "          update on the recent terminal exams that your son/daughter, "+student+",\n" +
                "          undertook at our college. I am pleased to present the results of these\n" +
                "          exams, which are detailed in the attached table. We believe that these\n" +
                "          results reflect "+student+"'s dedication and hard work in their studies.\n" +
                "        </div>\n" +
                "        <table\n" +
                "          border=\"1\"\n" +
                "          cellpadding=\"7\"\n" +
                "          cellspacing=\"0\"\n" +
                "          style=\"\n" +
                "            width: 100%;\n" +
                "            height: auto;\n" +
                "            border-color: rgb(4, 44, 68);\n" +
                "            margin-top: 50px;\n" +
                "            margin-bottom: 50px;\n" +
                "          \"\n" +
                "          width=\"100%\"\n" +
                "        >\n" +
                "          <tr>\n" +
                "            <td>SN</td>\n" +
                "            <td>Semester</td>\n" +
                "            <td>Marks</td>\n" +
                "            <td>Subject</td>\n" +
                "          </tr>";
        Integer count =1;
        try{
            while (result.next()){
                String subject = result.getString("sub_name");
                String marks = String.valueOf( result.getFloat("marks"));
                htmlcontent+= " <tr>\n" +
                        "          <td>"+count+"</td>\n" +
                        "          <td>"+sem+"</td>\n" +
                        "          <td>"+marks+"</td>\n" +
                        "          <td>"+subject+"</td>\n" +
                        "        </tr>";
                count++;
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        htmlcontent+=" </table>\n" +
                "\n" +
                "        <div class=\"footer\">\n" +
                "          If you need any further information or have any questions, please feel\n" +
                "          free to reach out to us. Thank you for your continued support of\n" +
                "          "+student+"'s education.\n" +
                "          <br /><br />\n" +
                "          Sincerely,\n" +
                "          <br />Campusflow BOT\n" +
                "        </div>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "  </body>\n" +
                "</html>\n";

        return htmlcontent;
    }
}
