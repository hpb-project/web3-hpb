pragma solidity ^0.4.25;

// Modified Greeter contract. Based on example at https://www.ethereum.org/greeter.

contract Mortal {
    
    address owner;

    
    constructor () public { owner = msg.sender; }

    
    function kill() public { if (msg.sender == owner) selfdestruct(owner); }
}

contract Greeter is Mortal {
    
    string greeting;

    
    constructor (string _greeting) public {
        greeting = _greeting;
    }

    function newGreeting(string _greeting) public {
        emit Modified(greeting, _greeting, greeting, _greeting);
        greeting = _greeting;
    }

    
    function greet() public constant returns (string)  {
        return greeting;
    }

    
    event Modified(
        string indexed oldGreetingIdx, string indexed newGreetingIdx,
        string oldGreeting, string newGreeting);
}