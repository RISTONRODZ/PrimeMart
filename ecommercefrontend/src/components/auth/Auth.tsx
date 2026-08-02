import LoginForm from "./LoginForm.tsx";
import signupBanner from "../../assets/signup_banner.png";
const Auth = () => {
    return (
       <div className={'flex flex-col items-center min-h-screen pt-16 pb-16'}>
            <div className={'max-w-md w-full h-auto bg-white rounded-lg shadow-lg p-6'}>
                <img className={'w-full rounded-t-md'} alt={'banner'} src={signupBanner} />
                <div className={'mt-8 px-10'}>
                    <LoginForm />
                </div>
            </div>
        </div>
    );
};
export default Auth;