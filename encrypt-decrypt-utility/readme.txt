=======================================================================
Step-1
BLOG: https://bonguides.com/how-to-install-openssl-in-windows-10-11/
Download: https://slproweb.com/products/Win32OpenSSL.html

Step-2
> Create a folder, open CMD
  - openssl genpkey -algorithm RSA -out private_key.pem -pkeyopt rsa_keygen_bits:2048
  - openssl rsa -pubout -in private_key.pem -out public_key.pem

Step-3 
Copy these files and paste in your project

Step-4
Run PemEncryptionDecryption.java code for testing.

========================================================================

1. Generate key-pair [Option1: using SSL]
   #generate a private key (RSA 2048 bits) and save it to key.pem
   openssl genpkey -algorithm RSA -out key.pem -aes256
   
   
   #extract the public key from the private key and save it to public.pem
   openssl rsa -in key.pem -outform PEM -pubout -out public.pem
   
2. Create Kubernetes Secret 
   Let's assume you have 'key.pem' and 'public.pem'
   
   kubectl create secret generic my-key-pair \
   	--from-file= key.pem=/path/to/key.pem \
   	--from-file= public.pem=/path/to/public.pem
   	
   	"This command creates a kubernetes secret named 'my-key-pair' and store the content of pub and private key"
   	
3. Mount secret in Pod
   In your k8s deployment config(ex- yaml file), you need to mount secret as volume in your pod specification
   
   *****
	apiVersion: v1
	kind: Pod
	metadata:
	  name: my-pod
	spec:
	  containers:
	    - name: my-container
	      image: my-image
	      volumeMounts:
	        - name: secret-volume
	          mountPath: "/path/to/keys"
	          readOnly: true
	  volumes:
	    - name: secret-volume
	      secret:
	        secretName: my-key-pair
   
   *****
   
   -We mount the Secret name 'my-key-pair' as a volume named 'secret-volume'.
   -The secret is mounted at the path
    '/path/to/keys' in the container
   -The volume is mounted as read-only to ensure that keys are not modified within the pod.
   
4. Access Keys in Spring Application
   In your spring application, you can access the keys at the specified mount path ('/path/to/keys')
   Load the keys during application startup and use them for encryption/decryption as needed.
   


   

  