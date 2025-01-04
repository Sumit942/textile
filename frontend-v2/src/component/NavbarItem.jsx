import React, { useState } from "react";
import { Link } from "react-router-dom";

// Recursive Component
const NavbarItem = ({ liClassName, item }) => {
    console.log('navbaritem')
  const [isOpen, setIsOpen] = useState(false)

  const handleMouseEnter = () => {
    // if (subMenuRef.current) {
    //     subMenuRef.current.style.display = 'block'
    // }
    setIsOpen(true)
  }

  const handleMouseLeave = () => {
    // if (subMenuRef.current) {
    //     subMenuRef.current.style.display = 'none'
    // }
    setIsOpen(false)
  }


  return (
    <>
      <li
        className="sd:hidden md:block relative"
        onMouseEnter={handleMouseEnter}
        onMouseLeave={handleMouseLeave}
      >
        {item.url ? (
          <Link
            to={item.url}
            className={`text-gray-300 hover:bg-gray-600 hover:text-white px-3 py-2 rounded-md text-sm font-medium flex items-center justify-between`}
          >
            <span className="flex-grow">{item.name}</span>
            {liClassName && item.children && item.children.length > 0 && (
              <span className="ml-2 text-gray-400">&#x25B6;</span> // Right arrow
            )}
          </Link>
        ) : (
          <div
            className={`text-gray-300 hover:bg-gray-600 hover:text-white px-3 py-2 rounded-md text-sm font-medium flex items-center justify-between`}
          >
            <span className="flex-grow">{item.name}</span>
            {liClassName && item.children && item.children.length > 0 && (
              <span className="ml-2 text-gray-400">&#x25B6;</span> // Right arrow
            )}
          </div>
        )}

        {item.children && item.children.length > 0 && (
          <div>
            <ul
              className={`absolute w-48 bg-gray-800 rounded-md shadow-lg transition-opacity duration-500 ease-in-out 
                ${isOpen ? "show " : "hide "}
                ${liClassName ? liClassName : ""}`}
            >
              {item.children.map((child) => (
                <NavbarItem
                  liClassName={"left-full top-0 mt-0"}
                  key={child.id}
                  item={child}
                />
              ))}
            </ul>
          </div>
        )}
      </li>
    </>
  );
};
export default NavbarItem;
