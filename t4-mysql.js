let mysql = require('mysql')
let lodash = require('lodash')
let fs = require('fs')
let ejs = require('ejs')
let { env } = require('process')
let cfg = {
    user: 'root',
    password: env.mysql_pwd,
    host: 'localhost',
    database: 'camundaworkflow'
}
let dictTypeMap = {};
dictTypeMap["text"]= "String";
dictTypeMap["longtext"]= "String";
dictTypeMap["ntext"]= "String";
dictTypeMap["char"]= "String";
dictTypeMap["nchar"]= "String";
dictTypeMap["varchar"]= "String";
dictTypeMap["nvarchar"]= "String";

dictTypeMap["uniqueidentifier"]= "java.util.UUID";

dictTypeMap["date"]= "java.sql.Date";
dictTypeMap["time"]= "java.sql.Time";
dictTypeMap["timestamp"]= "java.sql.Timestamp";
dictTypeMap["datetime"]= "java.sql.Timestamp";
dictTypeMap["datetime2"]= "java.util.Date";

dictTypeMap["bit"]= "Boolean";
dictTypeMap["tinyint"]= "Boolean";
dictTypeMap["int"]= "Integer";
dictTypeMap["smallint"]= "Integer";

dictTypeMap["real"]= "Double";
dictTypeMap["money"]= "Double";
dictTypeMap["double"]= "Double";
dictTypeMap["float"]= "Double";
dictTypeMap["decimal"]= "Double";
dictTypeMap["numeric"]= "Double";

dictTypeMap["bigint"]= "Long";

dictTypeMap["binary"]= "Byte[]";
dictTypeMap["image"]= "Byte[]";
dictTypeMap["longblob"]= "Byte[]";
dictTypeMap["blob"]= "Byte[]";

let templatestr = fs.readFileSync('./t4-mysql-template.ejs').toString('utf-8');
let packageName = "org.azeroth.mssqlserverclient";
let targetDir = "mssqlserverclient\\src\\main\\java\\org\\azeroth\\mssqlserverclient\\";

let cnn = mysql.createConnection(cfg)

cnn.connect();

let sql = `select * from INFORMATION_SCHEMA.TABLES  where TABLE_TYPE='BASE TABLE' and table_schema='${cfg.database}'`
let lstTable;
cnn.query(sql, function (err, res) {
    lstTable = res.map(x => x.TABLE_NAME);
    createBeanByTemplate(lstTable.shift(), cnn);
})

function createBeanByTemplate(tableName, cnn) {
    let sql1 = `select * from INFORMATION_SCHEMA.COLUMNS where TABLE_NAME='${tableName}'`
    let q1 = new Promise((ok, nok) => {
        cnn.query(sql1, (err, res) => {
            ok(res)
        })
    })
    let sql2 = `show full fields from ${tableName}`
    let q2 = new Promise((ok, nok) => {
        cnn.query(sql2, (err, res) => {
            ok(res)
        })
    })
    Promise.all([q1, q2]).then(wrapperdata => {
        wrapperdata[0].forEach(x => {
            x.Comment = wrapperdata[1].find(a => a.Field == x.COLUMN_NAME)?.Comment;
            x.DATA_TYPE=dictTypeMap[x.DATA_TYPE]||x.DATA_TYPE
        })
        let codestr = ejs.render(templatestr, { packageName, tableName, columns: wrapperdata[0] });
        fs.writeFile(targetDir + tableName + ".java", codestr, () => {
            console.log("ok--" + tableName)
            if (lstTable.length > 0) {
                createBeanByTemplate(lstTable.shift(), cnn);
            } else {
                cnn.end();
            }

        })
    })

}