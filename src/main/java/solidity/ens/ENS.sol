pragma solidity ^0.4.0;

import './AbstractENS.sol';


contract ENS is AbstractENS {
    struct Record {
        address owner;
        address resolver;
        uint64 ttl;
    }

    mapping(bytes32=>Record) records;

    // Permits modifications only by the owner of the specified node.
    modifier only_owner(bytes32 node) {
        if (records[node].owner != msg.sender) throw;
        _;
    }

    
    function ENS() {
        records[0].owner = msg.sender;
    }

    
    function owner(bytes32 node) constant returns (address) {
        return records[node].owner;
    }

    
    function resolver(bytes32 node) constant returns (address) {
        return records[node].resolver;
    }

    
    function ttl(bytes32 node) constant returns (uint64) {
        return records[node].ttl;
    }

    
    function setOwner(bytes32 node, address owner) only_owner(node) {
        Transfer(node, owner);
        records[node].owner = owner;
    }

    
    function setSubnodeOwner(bytes32 node, bytes32 label, address owner) only_owner(node) {
        var subnode = sha3(node, label);
        NewOwner(node, label, owner);
        records[subnode].owner = owner;
    }

    
    function setResolver(bytes32 node, address resolver) only_owner(node) {
        NewResolver(node, resolver);
        records[node].resolver = resolver;
    }

    
    function setTTL(bytes32 node, uint64 ttl) only_owner(node) {
        NewTTL(node, ttl);
        records[node].ttl = ttl;
    }
}
