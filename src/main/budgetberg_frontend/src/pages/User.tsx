import { useSelector } from "react-redux";

const User : React.FC = () => {
    const userRole = useSelector((state: any) => state.login.userRole);

    console.log(userRole.role);
    return (
        <div>
            user placeholder
        </div>
    )
}

export default User;