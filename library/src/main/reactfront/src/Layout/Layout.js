import Header from "./header/Header"
import Footer from "./Footer/Footer"
import Main from "./MainPage/Main"
import Category from "./Category/Category"

const Layout = () => {
    return (
        <div>
            <Category />
            <Header />
                <main>
                    <Main />
                </main>
        </div>
    )
}

export default Layout