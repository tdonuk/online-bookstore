import React from "react";
import {FaQuestion} from "react-icons/fa";
import PageHeader from "../fragment/PageHeader";

class AboutScreen extends React.Component {
    constructor(props) {
        super(props);

    }

    render() {
        return (
            <div className="container primary-background">
                <PageHeader/>
                <div className="big-icon">
                    <FaQuestion style={{color: "darkgray", fontSize: "160px"}}/>
                </div>
                <div className={"p-content"}>
                    <p>
                        <b>Bookstore</b> projesi, Yıldız Teknik Üniversitesi Matematik Mühendisliği bölümünde öğrenim gören
                        Resul Taha Dönük tarafından bitirme projesi olarak geliştirilmiştir. Projemiz microservices mimarisine örnek olarak
                        geliştirilmiş olup servisler arasında bir görev dağılımı yapılarak bir çeşit load balancing yöntemi kullanılmıştır.
                        Uygulamamız JWT tabanlı bir güvenlik altyapısı ile desteklenmiştir. Uygulamamız veri kaynağı olarak örnek olarak
                        eklenmiş olan 2 adet e-ticaret sitesini kullanmaktadır. Bunların isimleri verilmeyecektir.
                        <br/>
                        Proje kaynak kodları <a href={"https://github.com/tdonuk/online-bookstore"}>GitHub</a>'ta yayınlanmaktadır.
                    </p>
                    <br/>
                    <div>
                        <div><b>Yazar: </b> Resul Taha Dönük</div>
                        <div><b>Tarih: </b> Haziran, 2022</div>
                        <div><b>Yer: </b> İstanbul, Türkiye</div>
                    </div>
                    <br/>
                    <div><b>Ⓒ</b> Bu çalışmanın tüm hakları <b>Yıldız Teknik Üniversitesi</b>'ne aittir.</div>
                    <br/>
                    <br/>
                    <a href={"https://github.com/tdonuk/online-bookstore"}>https://github.com/tdonuk/online-bookstore</a>
                </div>
            </div>
        );
    }
}

AboutScreen.propTypes = {};

export default AboutScreen;