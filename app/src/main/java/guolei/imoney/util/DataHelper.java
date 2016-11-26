package guolei.imoney.util;

import java.util.ArrayList;

import guolei.imoney.application.MvpApplication;
import guolei.imoney.model.Expense;
import guolei.imoney.model.ExpenseListItem;
import guolei.imoney.model.db.DBClass;


/**
 * Created by guolei on 2016/5/31.
 */
public class DataHelper {
    public static ArrayList<ExpenseListItem> getTestData(){
        ArrayList<ExpenseListItem> data = new ArrayList<ExpenseListItem>();
        DBClass db = new DBClass(MvpApplication.getApplication());
        ArrayList<Expense> expenses =  db.queryExpense(null);
        System.out.println("dbnumber:"+db.queryNumber());
        for(int i = 0; i < expenses.size()-1; i++){
            ExpenseListItem ListItem = new ExpenseListItem(expenses.get(i));
            if(i == 0)
            {
                ExpenseListItem header = new ExpenseListItem(null);
                header.setSection(true,"一月");
                data.add(header);
            }
            // just for test
            if(i%10 == 0)
            {
                ExpenseListItem header = new ExpenseListItem(null);
                header.setSection(true,i/10 + "月");
                data.add(header);
            }

            if(expenses.get(i).getMonth() != expenses.get(i+1).getMonth()){
                ExpenseListItem header = new ExpenseListItem(null);
                header.setSection(true,expenses.get(i).getMonth()+"月");
                data.add(header);
            }
            ListItem.setSection(false,"null");
            ListItem.setSection(false,i/10 + "月");
            data.add(ListItem);
        }

        return data;
    }
    public static final String[] ITEMS = new String[] { "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea",

            "Eritrea", "Estonia", "Ethiopia", "Faeroe Islands", "Falkland Islands", "Fiji", "Finland", "Afghanistan",

            "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica",

            "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahrain",

            "Bangladesh", "Barbados", "Belarus", "Belgium", "Monaco", "Mongolia", "Montserrat", "Morocco",

            "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles",

            "New Caledonia", "New Zealand", "Guyana", "Haiti", "Heard Island and McDonald Islands", "Honduras",

            "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Israel", "Italy",

            "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Kuwait", "Kyrgyzstan", "Laos", "Latvia",

            "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Nicaragua", "Niger",

            "Nigeria", "Niue", "Norfolk Island", "North Korea", "Northern Marianas", "Norway", "Oman", "Pakistan",

            "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn Islands", "Poland",

            "Portugal", "Puerto Rico", "Qatar", "French Southern Territories", "Gabon", "Georgia", "Germany", "Ghana",

            "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea",

            "Guinea-Bissau", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia", "Moldova",

            "Bosnia and Herzegovina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory",

            "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Saudi Arabia", "Senegal", "Seychelles",

            "Sierra Leone", "Singapore", "Slovakia", "Slovenia", "Solomon Islands", "Somalia", "South Africa",

            "South Georgia and the South Sandwich Islands", "South Korea", "Spain", "Sri Lanka", "Sudan", "Suriname",

            "Svalbard and Jan Mayen", "Swaziland", "Sweden", "Switzerland", "Syria", "Taiwan", "Tajikistan",

            "Tanzania", "Thailand", "The Bahamas", "The Gambia", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago",

            "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine",

            "United Arab Emirates", "United Kingdom", "United States", "United States Minor Outlying Islands",

            "Uruguay", "Uzbekistan", "Vanuatu", "Vatican City", "Venezuela", "Vietnam", "Virgin Islands",

            "Wallis and Futuna", "Western Sahara", "British Virgin Islands", "Brunei", "Bulgaria", "Burkina Faso",

            "Burundi", "Cote d'Ivoire", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands",

            "Central African Republic", "Chad", "Chile", "China", "Reunion", "Romania", "Russia", "Rwanda",

            "Sqo Tome and Principe", "Saint Helena", "Saint Kitts and Nevis", "Saint Lucia",

            "Saint Pierre and Miquelon", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Christmas Island",

            "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo", "Cook Islands", "Costa Rica", "Croatia", "Cuba",

            "Cyprus", "Czech Republic", "Democratic Republic of the Congo", "Denmark", "Djibouti", "Dominica",

            "Dominican Republic", "Former Yugoslav Republic of Macedonia", "France", "French Guiana",

            "French Polynesia", "Macau", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta",

            "Marshall Islands", "Yemen", "Yugoslavia", "Zambia", "Zimbabwe" };

}
