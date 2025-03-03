```bash
kubectl create namespace argocd
kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml
```
* Change ClusterIP to LoadBalancer in order to access the ArgoCD UI

```bash
# Change the argo server from ClusterIP to LoadBalancer 
kubectl patch svc argocd-server -n argocd -p '{"spec": {"type": "LoadBalancer"}}'; echo ""
```

* Get ArgoCD admin password in K8s

```bash
kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath="{.data.password}" | base64 -d
```