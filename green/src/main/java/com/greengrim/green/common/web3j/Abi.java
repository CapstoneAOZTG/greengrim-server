package com.greengrim.green.common.web3j;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.5.2.
 */
@SuppressWarnings("rawtypes")
public class Abi extends Contract {
    public static final String BINARY = "608060405234801561000f575f80fd5b5060405161280238038061280283398181016040528101906100319190610281565b806040518060400160405280600981526020017f477265656e4772696d00000000000000000000000000000000000000000000008152506040518060400160405280600381526020017f4752470000000000000000000000000000000000000000000000000000000000815250815f90816100ac91906104e6565b5080600190816100bc91906104e6565b5050505f60065f6101000a81548160ff0219169083151502179055505f73ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff1603610148575f6040517f1e4fbdf700000000000000000000000000000000000000000000000000000000815260040161013f91906105c4565b60405180910390fd5b6101578161015e60201b60201c565b50506105dd565b5f600660019054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905081600660016101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508173ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a35050565b5f80fd5b5f73ffffffffffffffffffffffffffffffffffffffff82169050919050565b5f61025082610227565b9050919050565b61026081610246565b811461026a575f80fd5b50565b5f8151905061027b81610257565b92915050565b5f6020828403121561029657610295610223565b5b5f6102a38482850161026d565b91505092915050565b5f81519050919050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52604160045260245ffd5b7f4e487b71000000000000000000000000000000000000000000000000000000005f52602260045260245ffd5b5f600282049050600182168061032757607f821691505b60208210810361033a576103396102e3565b5b50919050565b5f819050815f5260205f209050919050565b5f6020601f8301049050919050565b5f82821b905092915050565b5f6008830261039c7fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff82610361565b6103a68683610361565b95508019841693508086168417925050509392505050565b5f819050919050565b5f819050919050565b5f6103ea6103e56103e0846103be565b6103c7565b6103be565b9050919050565b5f819050919050565b610403836103d0565b61041761040f826103f1565b84845461036d565b825550505050565b5f90565b61042b61041f565b6104368184846103fa565b505050565b5b818110156104595761044e5f82610423565b60018101905061043c565b5050565b601f82111561049e5761046f81610340565b61047884610352565b81016020851015610487578190505b61049b61049385610352565b83018261043b565b50505b505050565b5f82821c905092915050565b5f6104be5f19846008026104a3565b1980831691505092915050565b5f6104d683836104af565b9150826002028217905092915050565b6104ef826102ac565b67ffffffffffffffff811115610508576105076102b6565b5b6105128254610310565b61051d82828561045d565b5f60209050601f83116001811461054e575f841561053c578287015190505b61054685826104cb565b8655506105ad565b601f19841661055c86610340565b5f5b828110156105835784890151825560018201915060208501945060208101905061055e565b868310156105a0578489015161059c601f8916826104af565b8355505b6001600288020188555050505b505050505050565b6105be81610246565b82525050565b5f6020820190506105d75f8301846105b5565b92915050565b612218806105ea5f395ff3fe608060405234801561000f575f80fd5b506004361061012a575f3560e01c8063715018a6116100ab578063a22cb4651161006f578063a22cb465146102f4578063b88d4fde14610310578063c87b56dd1461032c578063e985e9c51461035c578063f2fde38b1461038c5761012a565b8063715018a6146102885780638456cb59146102925780638da5cb5b1461029c57806395d89b41146102ba578063a1448194146102d85761012a565b80633f4ba83a116100f25780633f4ba83a146101e457806342842e0e146101ee5780635c975abb1461020a5780636352211e1461022857806370a08231146102585761012a565b806301ffc9a71461012e57806306fdde031461015e578063081812fc1461017c578063095ea7b3146101ac57806323b872dd146101c8575b5f80fd5b61014860048036038101906101439190611a64565b6103a8565b6040516101559190611aa9565b60405180910390f35b610166610489565b6040516101739190611b32565b60405180910390f35b61019660048036038101906101919190611b85565b610518565b6040516101a39190611bef565b60405180910390f35b6101c660048036038101906101c19190611c32565b610533565b005b6101e260048036038101906101dd9190611c70565b610549565b005b6101ec610648565b005b61020860048036038101906102039190611c70565b61065a565b005b610212610679565b60405161021f9190611aa9565b60405180910390f35b610242600480360381019061023d9190611b85565b61068e565b60405161024f9190611bef565b60405180910390f35b610272600480360381019061026d9190611cc0565b61069f565b60405161027f9190611cfa565b60405180910390f35b610290610755565b005b61029a610768565b005b6102a461077a565b6040516102b19190611bef565b60405180910390f35b6102c26107a3565b6040516102cf9190611b32565b60405180910390f35b6102f260048036038101906102ed9190611c32565b610833565b005b61030e60048036038101906103099190611d3d565b610849565b005b61032a60048036038101906103259190611ea7565b61085f565b005b61034660048036038101906103419190611b85565b61087c565b6040516103539190611b32565b60405180910390f35b61037660048036038101906103719190611f27565b6108e2565b6040516103839190611aa9565b60405180910390f35b6103a660048036038101906103a19190611cc0565b610970565b005b5f7f80ac58cd000000000000000000000000000000000000000000000000000000007bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916827bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916148061047257507f5b5e139f000000000000000000000000000000000000000000000000000000007bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916827bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916145b806104825750610481826109f4565b5b9050919050565b60605f805461049790611f92565b80601f01602080910402602001604051908101604052809291908181526020018280546104c390611f92565b801561050e5780601f106104e55761010080835404028352916020019161050e565b820191905f5260205f20905b8154815290600101906020018083116104f157829003601f168201915b5050505050905090565b5f61052282610a5d565b5061052c82610ae3565b9050919050565b6105458282610540610b1c565b610b23565b5050565b5f73ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff16036105b9575f6040517f64a0ae920000000000000000000000000000000000000000000000000000000081526004016105b09190611bef565b60405180910390fd5b5f6105cc83836105c7610b1c565b610b35565b90508373ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff1614610642578382826040517f64283d7b00000000000000000000000000000000000000000000000000000000815260040161063993929190611fc2565b60405180910390fd5b50505050565b610650610b4a565b610658610bd1565b565b61067483838360405180602001604052805f81525061085f565b505050565b5f60065f9054906101000a900460ff16905090565b5f61069882610a5d565b9050919050565b5f8073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff1603610710575f6040517f89c62b640000000000000000000000000000000000000000000000000000000081526004016107079190611bef565b60405180910390fd5b60035f8373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f20549050919050565b61075d610b4a565b6107665f610c32565b565b610770610b4a565b610778610cf7565b565b5f600660019054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b6060600180546107b290611f92565b80601f01602080910402602001604051908101604052809291908181526020018280546107de90611f92565b80156108295780601f1061080057610100808354040283529160200191610829565b820191905f5260205f20905b81548152906001019060200180831161080c57829003601f168201915b5050505050905090565b61083b610b4a565b6108458282610d59565b5050565b61085b610854610b1c565b8383610d76565b5050565b61086a848484610549565b61087684848484610edf565b50505050565b606061088782610a5d565b505f610891611091565b90505f8151116108af5760405180602001604052805f8152506108da565b806108b9846110b1565b6040516020016108ca929190612057565b6040516020818303038152906040525b915050919050565b5f60055f8473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f8373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f9054906101000a900460ff16905092915050565b610978610b4a565b5f73ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff16036109e8575f6040517f1e4fbdf70000000000000000000000000000000000000000000000000000000081526004016109df9190611bef565b60405180910390fd5b6109f181610c32565b50565b5f7f01ffc9a7000000000000000000000000000000000000000000000000000000007bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916827bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916149050919050565b5f80610a688361117b565b90505f73ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff1603610ada57826040517f7e273289000000000000000000000000000000000000000000000000000000008152600401610ad19190611cfa565b60405180910390fd5b80915050919050565b5f60045f8381526020019081526020015f205f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050919050565b5f33905090565b610b3083838360016111b4565b505050565b5f610b41848484611373565b90509392505050565b610b52610b1c565b73ffffffffffffffffffffffffffffffffffffffff16610b7061077a565b73ffffffffffffffffffffffffffffffffffffffff1614610bcf57610b93610b1c565b6040517f118cdaa7000000000000000000000000000000000000000000000000000000008152600401610bc69190611bef565b60405180910390fd5b565b610bd9611390565b5f60065f6101000a81548160ff0219169083151502179055507f5db9ee0a495bf2e6ff9c91a7834c1ba4fdd244a5e8aa4e537bd38aeae4b073aa610c1b610b1c565b604051610c289190611bef565b60405180910390a1565b5f600660019054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905081600660016101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508173ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a35050565b610cff6113d0565b600160065f6101000a81548160ff0219169083151502179055507f62e78cea01bee320cd4e420270b5ea74000d11b0c9f74754ebdbfc544b05a258610d42610b1c565b604051610d4f9190611bef565b60405180910390a1565b610d72828260405180602001604052805f815250611411565b5050565b5f73ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff1603610de657816040517f5b08ba18000000000000000000000000000000000000000000000000000000008152600401610ddd9190611bef565b60405180910390fd5b8060055f8573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f8473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f6101000a81548160ff0219169083151502179055508173ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff167f17307eab39ab6107e8899845ad3d59bd9653f200f220920489ca2b5937696c3183604051610ed29190611aa9565b60405180910390a3505050565b5f8373ffffffffffffffffffffffffffffffffffffffff163b111561108b578273ffffffffffffffffffffffffffffffffffffffff1663150b7a02610f22610b1c565b8685856040518563ffffffff1660e01b8152600401610f4494939291906120db565b6020604051808303815f875af1925050508015610f7f57506040513d601f19601f82011682018060405250810190610f7c9190612139565b60015b611000573d805f8114610fad576040519150601f19603f3d011682016040523d82523d5f602084013e610fb2565b606091505b505f815103610ff857836040517f64a0ae92000000000000000000000000000000000000000000000000000000008152600401610fef9190611bef565b60405180910390fd5b805181602001fd5b63150b7a0260e01b7bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916817bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19161461108957836040517f64a0ae920000000000000000000000000000000000000000000000000000000081526004016110809190611bef565b60405180910390fd5b505b50505050565b60606040518060600160405280602a81526020016121b9602a9139905090565b60605f60016110bf8461142c565b0190505f8167ffffffffffffffff8111156110dd576110dc611d83565b5b6040519080825280601f01601f19166020018201604052801561110f5781602001600182028036833780820191505090505b5090505f82602001820190505b600115611170578080600190039150507f3031323334353637383961626364656600000000000000000000000000000000600a86061a8153600a858161116557611164612164565b5b0494505f850361111c575b819350505050919050565b5f60025f8381526020019081526020015f205f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050919050565b80806111ec57505f73ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff1614155b1561131e575f6111fb84610a5d565b90505f73ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff161415801561126557508273ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff1614155b8015611278575061127681846108e2565b155b156112ba57826040517fa9fbf51f0000000000000000000000000000000000000000000000000000000081526004016112b19190611bef565b60405180910390fd5b811561131c57838573ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff167f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b92560405160405180910390a45b505b8360045f8581526020019081526020015f205f6101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050505050565b5f61137c6113d0565b61138784848461157d565b90509392505050565b611398610679565b6113ce576040517f8dfc202b00000000000000000000000000000000000000000000000000000000815260040160405180910390fd5b565b6113d8610679565b1561140f576040517fd93c066500000000000000000000000000000000000000000000000000000000815260040160405180910390fd5b565b61141b8383611788565b6114275f848484610edf565b505050565b5f805f90507a184f03e93ff9f4daa797ed6e38ed64bf6a1f0100000000000000008310611488577a184f03e93ff9f4daa797ed6e38ed64bf6a1f010000000000000000838161147e5761147d612164565b5b0492506040810190505b6d04ee2d6d415b85acef810000000083106114c5576d04ee2d6d415b85acef810000000083816114bb576114ba612164565b5b0492506020810190505b662386f26fc1000083106114f457662386f26fc1000083816114ea576114e9612164565b5b0492506010810190505b6305f5e100831061151d576305f5e100838161151357611512612164565b5b0492506008810190505b612710831061154257612710838161153857611537612164565b5b0492506004810190505b60648310611565576064838161155b5761155a612164565b5b0492506002810190505b600a8310611574576001810190505b80915050919050565b5f806115888461117b565b90505f73ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff16146115c9576115c881848661187b565b5b5f73ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff1614611654576116085f855f806111b4565b600160035f8373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f82825403925050819055505b5f73ffffffffffffffffffffffffffffffffffffffff168573ffffffffffffffffffffffffffffffffffffffff16146116d357600160035f8773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f82825401925050819055505b8460025f8681526020019081526020015f205f6101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550838573ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef60405160405180910390a4809150509392505050565b5f73ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff16036117f8575f6040517f64a0ae920000000000000000000000000000000000000000000000000000000081526004016117ef9190611bef565b60405180910390fd5b5f61180483835f610b35565b90505f73ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff1614611876575f6040517f73c6ac6e00000000000000000000000000000000000000000000000000000000815260040161186d9190611bef565b60405180910390fd5b505050565b61188683838361193e565b611939575f73ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff16036118fa57806040517f7e2732890000000000000000000000000000000000000000000000000000000081526004016118f19190611cfa565b60405180910390fd5b81816040517f177e802f000000000000000000000000000000000000000000000000000000008152600401611930929190612191565b60405180910390fd5b505050565b5f8073ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff16141580156119f557508273ffffffffffffffffffffffffffffffffffffffff168473ffffffffffffffffffffffffffffffffffffffff1614806119b657506119b584846108e2565b5b806119f457508273ffffffffffffffffffffffffffffffffffffffff166119dc83610ae3565b73ffffffffffffffffffffffffffffffffffffffff16145b5b90509392505050565b5f604051905090565b5f80fd5b5f80fd5b5f7fffffffff0000000000000000000000000000000000000000000000000000000082169050919050565b611a4381611a0f565b8114611a4d575f80fd5b50565b5f81359050611a5e81611a3a565b92915050565b5f60208284031215611a7957611a78611a07565b5b5f611a8684828501611a50565b91505092915050565b5f8115159050919050565b611aa381611a8f565b82525050565b5f602082019050611abc5f830184611a9a565b92915050565b5f81519050919050565b5f82825260208201905092915050565b8281835e5f83830152505050565b5f601f19601f8301169050919050565b5f611b0482611ac2565b611b0e8185611acc565b9350611b1e818560208601611adc565b611b2781611aea565b840191505092915050565b5f6020820190508181035f830152611b4a8184611afa565b905092915050565b5f819050919050565b611b6481611b52565b8114611b6e575f80fd5b50565b5f81359050611b7f81611b5b565b92915050565b5f60208284031215611b9a57611b99611a07565b5b5f611ba784828501611b71565b91505092915050565b5f73ffffffffffffffffffffffffffffffffffffffff82169050919050565b5f611bd982611bb0565b9050919050565b611be981611bcf565b82525050565b5f602082019050611c025f830184611be0565b92915050565b611c1181611bcf565b8114611c1b575f80fd5b50565b5f81359050611c2c81611c08565b92915050565b5f8060408385031215611c4857611c47611a07565b5b5f611c5585828601611c1e565b9250506020611c6685828601611b71565b9150509250929050565b5f805f60608486031215611c8757611c86611a07565b5b5f611c9486828701611c1e565b9350506020611ca586828701611c1e565b9250506040611cb686828701611b71565b9150509250925092565b5f60208284031215611cd557611cd4611a07565b5b5f611ce284828501611c1e565b91505092915050565b611cf481611b52565b82525050565b5f602082019050611d0d5f830184611ceb565b92915050565b611d1c81611a8f565b8114611d26575f80fd5b50565b5f81359050611d3781611d13565b92915050565b5f8060408385031215611d5357611d52611a07565b5b5f611d6085828601611c1e565b9250506020611d7185828601611d29565b9150509250929050565b5f80fd5b5f80fd5b7f4e487b71000000000000000000000000000000000000000000000000000000005f52604160045260245ffd5b611db982611aea565b810181811067ffffffffffffffff82111715611dd857611dd7611d83565b5b80604052505050565b5f611dea6119fe565b9050611df68282611db0565b919050565b5f67ffffffffffffffff821115611e1557611e14611d83565b5b611e1e82611aea565b9050602081019050919050565b828183375f83830152505050565b5f611e4b611e4684611dfb565b611de1565b905082815260208101848484011115611e6757611e66611d7f565b5b611e72848285611e2b565b509392505050565b5f82601f830112611e8e57611e8d611d7b565b5b8135611e9e848260208601611e39565b91505092915050565b5f805f8060808587031215611ebf57611ebe611a07565b5b5f611ecc87828801611c1e565b9450506020611edd87828801611c1e565b9350506040611eee87828801611b71565b925050606085013567ffffffffffffffff811115611f0f57611f0e611a0b565b5b611f1b87828801611e7a565b91505092959194509250565b5f8060408385031215611f3d57611f3c611a07565b5b5f611f4a85828601611c1e565b9250506020611f5b85828601611c1e565b9150509250929050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52602260045260245ffd5b5f6002820490506001821680611fa957607f821691505b602082108103611fbc57611fbb611f65565b5b50919050565b5f606082019050611fd55f830186611be0565b611fe26020830185611ceb565b611fef6040830184611be0565b949350505050565b5f81905092915050565b5f61200b82611ac2565b6120158185611ff7565b9350612025818560208601611adc565b80840191505092915050565b7f2e6a736f6e000000000000000000000000000000000000000000000000000000815250565b5f6120628285612001565b915061206e8284612001565b915061207982612031565b6005820191508190509392505050565b5f81519050919050565b5f82825260208201905092915050565b5f6120ad82612089565b6120b78185612093565b93506120c7818560208601611adc565b6120d081611aea565b840191505092915050565b5f6080820190506120ee5f830187611be0565b6120fb6020830186611be0565b6121086040830185611ceb565b818103606083015261211a81846120a3565b905095945050505050565b5f8151905061213381611a3a565b92915050565b5f6020828403121561214e5761214d611a07565b5b5f61215b84828501612125565b91505092915050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52601260045260245ffd5b5f6040820190506121a45f830185611be0565b6121b16020830184611ceb565b939250505056fe68747470733a2f2f72616469616e742d6d616c6162692d6330313336322e6e65746c6966792e6170702fa26469706673582212200ed290ac2aacce7388556485f5afbddda390b855d37eac1bf4051d4cd33e16f764736f6c63430008190033";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_PAUSE = "pause";

    public static final String FUNC_RENOUNCEOWNERSHIP = "renounceOwnership";

    public static final String FUNC_SAFEMINT = "safeMint";

    public static final String FUNC_safeTransferFrom = "safeTransferFrom";

    public static final String FUNC_SETAPPROVALFORALL = "setApprovalForAll";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final String FUNC_UNPAUSE = "unpause";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_GETAPPROVED = "getApproved";

    public static final String FUNC_ISAPPROVEDFORALL = "isApprovedForAll";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_OWNEROF = "ownerOf";

    public static final String FUNC_PAUSED = "paused";

    public static final String FUNC_SUPPORTSINTERFACE = "supportsInterface";

    public static final String FUNC_SYMBOL = "symbol";

    public static final String FUNC_TOKENURI = "tokenURI";

    public static final Event APPROVAL_EVENT = new Event("Approval",
        Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>(true) {}));
    ;

    public static final Event APPROVALFORALL_EVENT = new Event("ApprovalForAll",
        Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Bool>() {}));
    ;

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred",
        Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event PAUSED_EVENT = new Event("Paused",
        Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event TRANSFER_EVENT = new Event("Transfer",
        Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>(true) {}));
    ;

    public static final Event UNPAUSED_EVENT = new Event("Unpaused",
        Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    @Deprecated
    protected Abi(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Abi(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Abi(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Abi(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> approve(String to, BigInteger tokenId) {
        final Function function = new Function(
            FUNC_APPROVE,
            Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, to),
                new org.web3j.abi.datatypes.generated.Uint256(tokenId)),
            Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static ApprovalEventResponse getApprovalEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(APPROVAL_EVENT, log);
        ApprovalEventResponse typedResponse = new ApprovalEventResponse();
        typedResponse.log = log;
        typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.approved = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.tokenId = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
        return typedResponse;
    }

    public Flowable<ApprovalEventResponse> approvalEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getApprovalEventFromLog(log));
    }

    public Flowable<ApprovalEventResponse> approvalEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVAL_EVENT));
        return approvalEventFlowable(filter);
    }

    public static ApprovalForAllEventResponse getApprovalForAllEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(APPROVALFORALL_EVENT, log);
        ApprovalForAllEventResponse typedResponse = new ApprovalForAllEventResponse();
        typedResponse.log = log;
        typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.operator = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.approved = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<ApprovalForAllEventResponse> approvalForAllEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getApprovalForAllEventFromLog(log));
    }

    public Flowable<ApprovalForAllEventResponse> approvalForAllEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVALFORALL_EVENT));
        return approvalForAllEventFlowable(filter);
    }


    public static OwnershipTransferredEventResponse getOwnershipTransferredEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, log);
        OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
        typedResponse.log = log;
        typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getOwnershipTransferredEventFromLog(log));
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(OWNERSHIPTRANSFERRED_EVENT));
        return ownershipTransferredEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> pause() {
        final Function function = new Function(
            FUNC_PAUSE,
            Arrays.<Type>asList(),
            Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static PausedEventResponse getPausedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(PAUSED_EVENT, log);
        PausedEventResponse typedResponse = new PausedEventResponse();
        typedResponse.log = log;
        typedResponse.account = (String) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<PausedEventResponse> pausedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getPausedEventFromLog(log));
    }

    public Flowable<PausedEventResponse> pausedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PAUSED_EVENT));
        return pausedEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> renounceOwnership() {
        final Function function = new Function(
            FUNC_RENOUNCEOWNERSHIP,
            Arrays.<Type>asList(),
            Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> safeMint(String to, BigInteger tokenId) {
        final Function function = new Function(
            FUNC_SAFEMINT,
            Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, to),
                new org.web3j.abi.datatypes.generated.Uint256(tokenId)),
            Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> safeTransferFrom(String from, String to, BigInteger tokenId) {
        final Function function = new Function(
            FUNC_safeTransferFrom,
            Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, from),
                new org.web3j.abi.datatypes.Address(160, to),
                new org.web3j.abi.datatypes.generated.Uint256(tokenId)),
            Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> safeTransferFrom(String from, String to, BigInteger tokenId, byte[] data) {
        final Function function = new Function(
            FUNC_safeTransferFrom,
            Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, from),
                new org.web3j.abi.datatypes.Address(160, to),
                new org.web3j.abi.datatypes.generated.Uint256(tokenId),
                new org.web3j.abi.datatypes.DynamicBytes(data)),
            Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setApprovalForAll(String operator, Boolean approved) {
        final Function function = new Function(
            FUNC_SETAPPROVALFORALL,
            Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, operator),
                new org.web3j.abi.datatypes.Bool(approved)),
            Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static TransferEventResponse getTransferEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(TRANSFER_EVENT, log);
        TransferEventResponse typedResponse = new TransferEventResponse();
        typedResponse.log = log;
        typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.tokenId = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
        return typedResponse;
    }

    public Flowable<TransferEventResponse> transferEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getTransferEventFromLog(log));
    }

    public Flowable<TransferEventResponse> transferEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT));
        return transferEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> transferFrom(String from, String to, BigInteger tokenId) {
        final Function function = new Function(
            FUNC_TRANSFERFROM,
            Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, from),
                new org.web3j.abi.datatypes.Address(160, to),
                new org.web3j.abi.datatypes.generated.Uint256(tokenId)),
            Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> transferOwnership(String newOwner) {
        final Function function = new Function(
            FUNC_TRANSFEROWNERSHIP,
            Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, newOwner)),
            Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> unpause() {
        final Function function = new Function(
            FUNC_UNPAUSE,
            Arrays.<Type>asList(),
            Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static UnpausedEventResponse getUnpausedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(UNPAUSED_EVENT, log);
        UnpausedEventResponse typedResponse = new UnpausedEventResponse();
        typedResponse.log = log;
        typedResponse.account = (String) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<UnpausedEventResponse> unpausedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getUnpausedEventFromLog(log));
    }

    public Flowable<UnpausedEventResponse> unpausedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(UNPAUSED_EVENT));
        return unpausedEventFlowable(filter);
    }

    public RemoteFunctionCall<BigInteger> balanceOf(String owner) {
        final Function function = new Function(FUNC_BALANCEOF,
            Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, owner)),
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> getApproved(BigInteger tokenId) {
        final Function function = new Function(FUNC_GETAPPROVED,
            Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(tokenId)),
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<Boolean> isApprovedForAll(String owner, String operator) {
        final Function function = new Function(FUNC_ISAPPROVEDFORALL,
            Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, owner),
                new org.web3j.abi.datatypes.Address(160, operator)),
            Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<String> name() {
        final Function function = new Function(FUNC_NAME,
            Arrays.<Type>asList(),
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> owner() {
        final Function function = new Function(FUNC_OWNER,
            Arrays.<Type>asList(),
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> ownerOf(BigInteger tokenId) {
        final Function function = new Function(FUNC_OWNEROF,
            Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(tokenId)),
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<Boolean> paused() {
        final Function function = new Function(FUNC_PAUSED,
            Arrays.<Type>asList(),
            Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> supportsInterface(byte[] interfaceId) {
        final Function function = new Function(FUNC_SUPPORTSINTERFACE,
            Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes4(interfaceId)),
            Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<String> symbol() {
        final Function function = new Function(FUNC_SYMBOL,
            Arrays.<Type>asList(),
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> tokenURI(BigInteger tokenId) {
        final Function function = new Function(FUNC_TOKENURI,
            Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(tokenId)),
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    @Deprecated
    public static Abi load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Abi(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Abi load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Abi(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Abi load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Abi(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Abi load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Abi(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Abi> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String initialOwner) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, initialOwner)));
        return deployRemoteCall(Abi.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<Abi> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String initialOwner) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, initialOwner)));
        return deployRemoteCall(Abi.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Abi> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String initialOwner) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, initialOwner)));
        return deployRemoteCall(Abi.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Abi> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String initialOwner) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, initialOwner)));
        return deployRemoteCall(Abi.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class ApprovalEventResponse extends BaseEventResponse {
        public String owner;

        public String approved;

        public BigInteger tokenId;
    }

    public static class ApprovalForAllEventResponse extends BaseEventResponse {
        public String owner;

        public String operator;

        public Boolean approved;
    }

    public static class OwnershipTransferredEventResponse extends BaseEventResponse {
        public String previousOwner;

        public String newOwner;
    }

    public static class PausedEventResponse extends BaseEventResponse {
        public String account;
    }

    public static class TransferEventResponse extends BaseEventResponse {
        public String from;

        public String to;

        public BigInteger tokenId;
    }

    public static class UnpausedEventResponse extends BaseEventResponse {
        public String account;
    }
}

